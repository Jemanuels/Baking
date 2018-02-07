package za.co.samtakie.baking.data;

import java.util.ArrayList;
import java.util.List;
import za.co.samtakie.baking.R;


/**
 * Created by jemanuels on 2018/01/28.
 * Last updated 2018/02/02
 * BakingImages class object to hold all images resources for the recipes.
 */
public class BakingImages {

    /* Create a static variable to hold all images we have in the resources linked to
    * the recipe id */
    private static final List<Integer> bakingImages = new ArrayList<Integer>(){{
        add(R.drawable.nutella_pie);
        add(R.drawable.brownies);
        add(R.drawable.yellow_cake);
        add(R.drawable.cheese_cake);
    }};

    /* return all images resources in the array, or get specific images by calling get(int index) */
    public static List<Integer> getBakingImages() {
        return bakingImages;
    }
}