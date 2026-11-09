package edu.charlotte.evaluation03.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.charlotte.evaluation03.databinding.FragmentCategoriesBinding;
import edu.charlotte.evaluation03.databinding.ItemCategoryBinding;
import edu.charlotte.evaluation03.models.Category;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CategoriesFragment extends Fragment {
    public CategoriesFragment() {
        // Required empty public constructor
    }

    FragmentCategoriesBinding binding;
    OkHttpClient client = new OkHttpClient();
    ArrayList<Category> categories = new ArrayList<>();
    CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Categories");

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelCategorySelection();
            }
        });

        adapter = new CategoryAdapter(getContext(), categories);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mListener.sendBackSelectedCategory(categories.get(position));
            }
        });

        getCategories();
    }

    void getCategories() {
        Request request = new Request.Builder()
                .url("https://www.themealdb.com/api/json/v1/1/categories.php")
                .build();

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
                    JSONArray arr = root.getJSONArray("categories");
                    ArrayList<Category> temp = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        temp.add(new Category(arr.getJSONObject(i)));
                    }
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            categories.clear();
                            categories.addAll(temp);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class CategoryAdapter extends ArrayAdapter<Category> {
        public CategoryAdapter(@NonNull Context context, @NonNull List<Category> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ItemCategoryBinding b;
            if (convertView == null) {
                b = ItemCategoryBinding.inflate(LayoutInflater.from(getContext()), parent, false);
                convertView = b.getRoot();
                convertView.setTag(b);
            } else {
                b = (ItemCategoryBinding) convertView.getTag();
            }
            Category c = getItem(position);
            b.textViewName.setText(c.strCategory);
            Glide.with(getContext()).load(c.strCategoryThumb).into(b.imageView);
            return convertView;
        }
    }

    CategoriesListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (CategoriesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CategoriesListener");
        }
    }

    public interface CategoriesListener {
        void sendBackSelectedCategory(Category category);
        void cancelCategorySelection();
    }
}