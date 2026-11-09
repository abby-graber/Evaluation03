package edu.charlotte.evaluation03;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import edu.charlotte.evaluation03.fragments.CategoriesFragment;
import edu.charlotte.evaluation03.fragments.MealDetailsFragment;
import edu.charlotte.evaluation03.fragments.MyCookBookFragment;
import edu.charlotte.evaluation03.fragments.SearchFragment;
import edu.charlotte.evaluation03.fragments.auth.LoginFragment;
import edu.charlotte.evaluation03.fragments.auth.RegisterFragment;
import edu.charlotte.evaluation03.models.Category;
import edu.charlotte.evaluation03.models.MealDetail;

public class MainActivity extends AppCompatActivity implements SearchFragment.SearchListener, MyCookBookFragment.CookBookListener, CategoriesFragment.CategoriesListener, LoginFragment.LoginListener, RegisterFragment.RegisterListener {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);

        if (mAuth.getCurrentUser() == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootView, new SearchFragment(), "search-fragment")
                    .commit();
        }


    }

    @Override
    public void gotoSelectCategory() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CategoriesFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoMyCookBook() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new MyCookBookFragment())
                .commit();
    }

    @Override
    public void goBackToSearch() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SearchFragment(), "search-fragment")
                .commit();
    }

    @Override
    public void gotoMealDetails(MealDetail mealDetail) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, MealDetailsFragment.newInstance(mealDetail))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        mAuth.signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void sendBackSelectedCategory(Category category) {
        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("search-fragment");
        searchFragment.setSelectedCategory(category);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelCategorySelection() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoSearchScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SearchFragment(), "search-fragment")
                .commit();
    }

    @Override
    public void gotoLoginScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoRegisterScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegisterFragment())
                .commit();
    }
}