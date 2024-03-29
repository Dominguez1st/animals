package edu.cnm.deepdive.animals.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.animals.R;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.viewmodel.AnimalViewModel;
import java.util.List;
import java.util.Objects;

public class ImageFragment extends Fragment implements OnItemSelectedListener {

  private ImageView imageView;
  private AnimalViewModel animalViewModel;
  private Spinner spinner;
  private List<Animal> animals;
  private Toolbar toolbar;
  private int selectedAnimal = -1;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image, container, false);
    toolbar = root.findViewById(R.id.toolbar);
    imageView = root.findViewById(R.id.image_view);
    toolbar.setTitle(R.string.app_name);
    spinner = root.findViewById(R.id.animals_spinner);
    spinner.setOnItemSelectedListener(this);
    ((MainActivity)getActivity()).setSupportActionBar(toolbar);
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    animalViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
        .get(AnimalViewModel.class);
    animalViewModel.getAnimals().observe(getViewLifecycleOwner(), new Observer<List<Animal>>() {
      @Override
      public void onChanged(List<Animal> animals) {
        ImageFragment.this.animals = animals;
        ArrayAdapter<Animal> adapter = new ArrayAdapter<>(
            ImageFragment.this.getContext(), R.layout.custom_spinner_item, animals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (selectedAnimal >= 0) {
          updateSelection();
        }

      }
    });

    animalViewModel.getSelectedItem().observe(getViewLifecycleOwner(), (item) -> {
      if (item != selectedAnimal) {
        selectedAnimal = item;
        if (animals != null) {
          updateSelection();
        }
      }
    });
  }


  private void updateSelection() {
    spinner.setSelection(selectedAnimal);

    Picasso.get().load(animals.get(selectedAnimal).getImageUrl())
        .into(imageView);

  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    animalViewModel.select(position);
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }
}