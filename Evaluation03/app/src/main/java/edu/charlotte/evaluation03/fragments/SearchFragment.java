package edu.charlotte.evaluation03.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.charlotte.evaluation03.R;
import edu.charlotte.evaluation03.databinding.FragmentSearchBinding;
import edu.charlotte.evaluation03.databinding.ItemSearchMealBinding;
import edu.charlotte.evaluation03.models.Category;
import edu.charlotte.evaluation03.models.Meal;
import edu.charlotte.evaluation03.models.MealDetail;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {
    public SearchFragment() {
        // Required empty public constructor
    }

    FragmentSearchBinding binding;
    Category selectedCategory;
    ArrayList<Meal> meals = new ArrayList<>();
    OkHttpClient client = new OkHttpClient();
    MealAdapter adapter;

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search");

        // restore the selected category text when coming back from the Categories screen
        if (selectedCategory != null) {
            binding.textViewCategory.setText(selectedCategory.strCategory);
        }

        // list + adapter (re-attached every time the view is created so results survive navigation)
        adapter = new MealAdapter(getContext(), meals);
        binding.listView.setAdapter(adapter);

        // clicking a row -> lookup full meal -> go to Meal Details
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                lookupMeal(meals.get(position).idMeal);
            }
        });

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = null;
                binding.textViewCategory.setText("No Category Selected");
                binding.editTextSearch.setText("");
                meals.clear();
                adapter.notifyDataSetChanged();
            }
        });

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = binding.editTextSearch.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    // keyword wins over category
                    fetchMeals("search.php", "s", keyword);
                } else if (selectedCategory != null) {
                    fetchMeals("filter.php", "c", selectedCategory.strCategory);
                } else {
                    Toast.makeText(getContext(), "Enter a keyword or select a category", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoSelectCategory();
            }
        });

        binding.buttonCookBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoMyCookBook();
            }
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@org.jspecify.annotations.NonNull Menu menu, @org.jspecify.annotations.NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@org.jspecify.annotations.NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_logout) {
                    mListener.logout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    // Calls search.php?s=... or filter.php?c=... and fills the list
    void fetchMeals(String endpoint, String param, String value) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("www.themealdb.com")
                .addPathSegments("api/json/v1/1/" + endpoint)
                .addQueryParameter(param, value)
                .build();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) return;
                String body = response.body().string();
                try {
                    JSONObject root = new JSONObject(body);
                    ArrayList<Meal> temp = new ArrayList<>();
                    // API returns "meals": null when nothing is found
                    if (!root.isNull("meals")) {
                        JSONArray arr = root.getJSONArray("meals");
                        for (int i = 0; i < arr.length(); i++) {
                            temp.add(new Meal(arr.getJSONObject(i)));
                        }
                    }
                    Activity activity = getActivity();
                    if (activity == null) return;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            meals.clear();
                            meals.addAll(temp);
                            adapter.notifyDataSetChanged();
                            if (temp.isEmpty()) {
                                Toast.makeText(activity, "No meals found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Calls lookup.php?i=... then sends the full MealDetail to the details screen
    void lookupMeal(String idMeal) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("www.themealdb.com")
                .addPathSegments("api/json/v1/1/lookup.php")
                .addQueryParameter("i", idMeal)
                .build();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) return;
                String body = response.body().string();
                try {
                    JSONObject root = new JSONObject(body);
                    JSONArray arr = root.getJSONArray("meals");
                    MealDetail detail = new MealDetail(arr.getJSONObject(0));
                    Log.d("demo", "Looked up: " + detail.strMeal + " ingredients=" + detail.ingredients.size());

                    Activity activity = getActivity();
                    if (activity == null) return;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.gotoMealDetails(detail);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MealAdapter extends ArrayAdapter<Meal> {
        public MealAdapter(@NonNull Context context, @NonNull List<Meal> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ItemSearchMealBinding b;
            if (convertView == null) {
                b = ItemSearchMealBinding.inflate(LayoutInflater.from(getContext()), parent, false);
                convertView = b.getRoot();
                convertView.setTag(b);
            } else {
                b = (ItemSearchMealBinding) convertView.getTag();
            }
            Meal m = getItem(position);
            b.textViewName.setText(m.strMeal);
            b.textViewArea.setText(m.strArea);
            Glide.with(getContext()).load(m.strMealThumb).into(b.imageView);
            return convertView;
        }
    }

    SearchListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (SearchListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SearchListener");
        }
    }

    public interface SearchListener {
        void gotoSelectCategory();
        void gotoMyCookBook();
        void gotoMealDetails(MealDetail mealDetail);
        void logout();
    }
}