package edu.dgu.coffeemaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.dgu.coffeemaker.exceptions.InventoryException;
import edu.dgu.coffeemaker.exceptions.RecipeException;

public class Main {
	private static CoffeeMaker coffeeMaker;

	public static void mainMenu() {
		System.out.println("1. Add a recipe");
		System.out.println("2. Delete a recipe");
		System.out.println("3. Edit a recipe");
		System.out.println("4. Add inventory");
		System.out.println("5. Check inventory");
		System.out.println("6. Make coffee");
		System.out.println("0. Exit\n");

		try {
			int userInput = Integer.parseInt(inputOutput(
					"Please press the number that corresponds to what you would like the coffee maker to do."));

			if (userInput >= 0 && userInput <= 6) {
				if (userInput == 1)
					addRecipe();
				if (userInput == 2)
					deleteRecipe();
				if (userInput == 3)
					editRecipe();
				if (userInput == 4)
					addInventory();
				if (userInput == 5)
					checkInventory();
				if (userInput == 6)
					makeCoffee();
				if (userInput == 0)
					System.exit(0);
			} else {
				System.out.println("Please enter a number from 0 - 6");
				mainMenu();
			}
		} catch (NumberFormatException e) {
			System.out.println("Please enter a number from 0 - 6");
			mainMenu();
		}
	}

	public static void addRecipe() {
		String name = inputOutput("\nPlease enter the recipe name: ");
		String priceString = inputOutput("\nPlease enter the recipe price: $");
		String coffeeString = inputOutput("\nPlease enter the units of coffee in the recipe: ");
		String milkString = inputOutput("\nPlease enter the units of milk in the recipe: ");
		String sugarString = inputOutput("\nPlease enter the units of sugar in the recipe: ");
		String chocolateString = inputOutput("\nPlease enter the units of chocolate in the recipe: ");

		Recipe r = new Recipe();
		try {
			r.setName(name);
			r.setPrice(priceString);
			r.setAmtCoffee(coffeeString);
			r.setAmtMilk(milkString);
			r.setAmtSugar(sugarString);
			r.setAmtChocolate(chocolateString);

			boolean recipeAdded = coffeeMaker.addRecipe(r);

			if (recipeAdded) {
				System.out.println(name + " successfully added.\n");
			} else {
				System.out.println(name + " could not be added.\n");
			}
		} catch (RecipeException e) {
			System.out.println(e.getMessage());
		} finally {
			mainMenu();
		}
	}

	public static void deleteRecipe() {
		Recipe[] recipes = coffeeMaker.getRecipes();
		for (int i = 0; i < recipes.length; i++) {
			if (recipes[i] != null) {
				System.out.println((i + 1) + ". " + recipes[i].getName());
			}
		}
		int recipeToDelete = recipeListSelection("Please select the number of the recipe to delete.");

		if (recipeToDelete < 0) {
			mainMenu();
		}

		String recipeDeleted = coffeeMaker.deleteRecipe(recipeToDelete);

		if (recipeDeleted != null) {
			System.out.println(recipeDeleted + " successfully deleted.\n");
		} else {
			System.out.println("Selected recipe doesn't exist and could not be deleted.\n");
		}
		mainMenu();
	}

	public static void editRecipe() {
		Recipe[] recipes = coffeeMaker.getRecipes();
		for (int i = 0; i < recipes.length; i++) {
			if (recipes[i] != null) {
				System.out.println((i + 1) + ". " + recipes[i].getName());
			}
		}
		int recipeToEdit = recipeListSelection("Please select the number of the recipe to edit.");

		if (recipeToEdit < 0) {
			mainMenu();
		}

		String priceString = inputOutput("\nPlease enter the recipe price: $");
		String coffeeString = inputOutput("\nPlease enter the units of coffee in the recipe: ");
		String milkString = inputOutput("\nPlease enter the units of milk in the recipe: ");
		String sugarString = inputOutput("\nPlease enter the units of sugar in the recipe: ");
		String chocolateString = inputOutput("\nPlease enter the units of chocolate in the recipe: ");

		Recipe newRecipe = new Recipe();
		try {
			newRecipe.setPrice(priceString);
			newRecipe.setAmtCoffee(coffeeString);
			newRecipe.setAmtMilk(milkString);
			newRecipe.setAmtSugar(sugarString);
			newRecipe.setAmtChocolate(chocolateString);

			String recipeEdited = coffeeMaker.editRecipe(recipeToEdit, newRecipe);

			if (recipeEdited != null) {
				System.out.println(recipeEdited + " successfully edited.\n");
			} else {
				System.out.println(recipeEdited + "could not be edited.\n");
			}
		} catch (RecipeException e) {
			System.out.println(e.getMessage());
		} finally {
			mainMenu();
		}
	}

	public static void addInventory() {
		String coffeeString = inputOutput("\nPlease enter the units of coffee to add: ");
		String milkString = inputOutput("\nPlease enter the units of milk to add: ");
		String sugarString = inputOutput("\nPlease enter the units of sugar to add: ");
		String chocolateString = inputOutput("\nPlease enter the units of chocolate to add: ");

		try {
			coffeeMaker.addInventory(coffeeString, milkString, sugarString, chocolateString);
			System.out.println("Inventory successfully added");
		} catch (InventoryException e) {
			System.out.println("Inventory was not added");
		} finally {
			mainMenu();
		}
	}

	public static void checkInventory() {
		System.out.println(coffeeMaker.checkInventory());
		mainMenu();
	}

	public static void makeCoffee() {
		Recipe[] recipes = coffeeMaker.getRecipes();
		for (int i = 0; i < recipes.length; i++) {
			if (recipes[i] != null) {
				System.out.println((i + 1) + ". " + recipes[i].getName());
			}
		}

		int recipeToPurchase = recipeListSelection("Please select the number of the recipe to purchase.");

		String amountPaid = inputOutput("Please enter the amount you wish to pay");
		int amtPaid = 0;
		try {
			amtPaid = Integer.parseInt(amountPaid);
		} catch (NumberFormatException e) {
			System.out.println("Please enter a positive integer");
			mainMenu();
		}

		int change = coffeeMaker.makeCoffee(recipeToPurchase, amtPaid);

		if (change == amtPaid) {
			System.out.println("Insufficient funds to purchase.");
		} else {
			System.out.println("Thank you for purchasing " + coffeeMaker.getRecipes()[recipeToPurchase].getName());
		}
		System.out.println("Your change is: " + change + "\n");
		mainMenu();
	}

	private static String inputOutput(String message) {
		System.out.println(message);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String returnString = "";
		try {
			returnString = br.readLine();
		} catch (IOException e) {
			System.out.println("Error reading in value");
			mainMenu();
		}
		return returnString;
	}

	private static int recipeListSelection(String message) {
		String userSelection = inputOutput(message);
		int recipe = 0;
		try {
			recipe = Integer.parseInt(userSelection) - 1;
			if (recipe >= 0 && recipe <= 2) {
			} else {
				recipe = -1;
			}
		} catch (NumberFormatException e) {
			System.out.println("Please select a number from 1-3.");
			recipe = -1;
		}
		return recipe;
	}

	public static void main(String[] args) {
		coffeeMaker = new CoffeeMaker();
		System.out.println("Welcome to the CoffeeMaker!\n");
		mainMenu();
	}
}
