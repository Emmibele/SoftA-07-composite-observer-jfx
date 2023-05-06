package ohm.softa.a07.controllers;

import javafx.application.Platform;
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

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import ohm.softa.a07.model.Meal;
public class MainController implements Initializable {

	private OpenMensaAPI openMensaAPI;
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnClose;

	@FXML
	private CheckBox chkVegetarian;

	@FXML
	private ListView<Meal> mealsList;

	private ObservableList<Meal> observableMeals;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("https://openmensa.org/api/v2/")
			.build();
		openMensaAPI = retrofit.create(OpenMensaAPI.class);

		observableMeals = mealsList.getItems();

		// set the event handler (callback)
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Call<List<Meal>> mealCall = openMensaAPI.getMeals(dateFormat.format(new Date()));
				mealCall.enqueue(new Callback<List<Meal>>() {
					@Override
					public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
						if (response.isSuccessful() && response.body() != null) {
							Platform.runLater(() -> {
								observableMeals.clear();
								observableMeals.addAll(response.body());
							});
						}
					}

					@Override
					public void onFailure(Call<List<Meal>> call, Throwable t) {
						observableMeals.clear();
					}
				});
			}
		});

		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});

	}
}
