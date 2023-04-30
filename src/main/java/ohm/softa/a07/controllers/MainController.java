package ohm.softa.a07.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import ohm.softa.a07.api.OpenMensaAPI;
import ohm.softa.a07.model.Meal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

	private OpenMensaAPI openMensaAPI;



	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnClose;

	@FXML
	private CheckBox chkVegetarian;

	@FXML
	private ListView<String> mealsList;


	@Override
	public void initialize(URL location, ResourceBundle resources) {


		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("https://openmensa.org/api/v2/")
			.build();

		openMensaAPI = retrofit.create(OpenMensaAPI.class);





		// set the event handler (callback)
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Call<List<Meal>> mealCall = openMensaAPI.getMeals("2023-05-02");
				mealCall.enqueue(new Callback<List<Meal>>() {
					@Override
					public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
						List<Meal> meals = response.body();
						ObservableList<String> stringsMeals = FXCollections.observableArrayList();
						for(Meal m : meals){
							stringsMeals.add(m.toString());
						}
						mealsList.setItems((ObservableList<String>) stringsMeals);
					}

					@Override
					public void onFailure(Call<List<Meal>> call, Throwable t) {
						ObservableList<String> list = FXCollections.observableArrayList("Enqueue failure");
						mealsList.setItems(list);
					}
				});



				//ObservableList<String> list = FXCollections.observableArrayList("Hans", "Dampf");
				//mealsList.setItems(list);
			}
		});
	}
}
