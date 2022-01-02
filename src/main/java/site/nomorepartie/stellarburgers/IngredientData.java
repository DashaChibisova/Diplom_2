package site.nomorepartie.stellarburgers;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IngredientData {

    static ArrayList<String> invalidDataIngr = new ArrayList<>(Arrays.asList(RandomStringUtils.randomAlphabetic(5)));

    public IngredientData(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientData setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public ArrayList<String> ingredients;

    public static ArrayList<String> getIngredient(int count) {
        final ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ArrayList<HashMap<String, String>> ingredient = new Order().getIngredient().extract().path("data");
            ingredients.add(ingredient.get(i).get("_id"));
        }
        return ingredients;
    }
}
