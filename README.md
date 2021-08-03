# Cooking-Companion
A recipe app for the home chef, by Reed Olm.

# Functionality
- Cooking Companion allows you to create, browse, search, edit, view, and delete your own recipes for a nice way to store and access them all.
- **Creating** a recipe from scratch means you control what ingredients and amounts you use, what serving size and the calorie content of the
meal is, as well as the preparation instructions for the meal. There is also a naming and tagging system, that allows you to add custom tags to a recipe, 
to allow you to group and find your recipies later. For example, say you have a turkey meal recipe, and a pumpkin pie recipe; adding a 
'Thanksgiving' tag to your reipe will allow you to see both of them during a search.
- **Browsing** a recipe allows you to easily scroll through all of your stored recipes, seeing their names, and the calorie content per serving. Nice and simple!
- **Searching** for a recipe allows you to search by 4 main ways
    - by Name: Enter a name, and any recipes that include what you typed in their name will appear.
    - by Ingredient: Enter the name of an ingredient, and any recipes that include that ingredient will appear.
    - by Tag: Enter a tag and any recipe you've tagged with that tag will appear.
    - by Calories: Enter a number, and only recipies with a calorie count per serving BELOW the number you gave will appear.
- **Editing** a recipe means you can go back and tweak the directions, or the amount of an ingredient, or anything with ease. Just make sure you save before you quit!
- **Viewing** a state of the art view page, allows you to check off ingredients as you prepare them as well as check off instructions as you complete them.
- **Deleting** a recipe from the view screen will remove it from the list of recipes. After deleting, if you click the confirm button that appears, it will permanently
save what you just did, so be careful!

# Starting the program
- Just run the program, Cooking Companion does the rest.

# Files warning
- Cooking Companion creates and uses two .csv files. Do not edit them unless you know what you're doing with them :)
- If you mess with the formatting, the contents will be overwritten, and you will lose all your saved recipes so don't do it

# TODO
- [x] Home view
- [x] Browse view
- [ ] Create view
  - [x] Allow user to create recipes
  - [ ] Produce success/error messages when the user interacts.
- [ ] Edit view
  - [x] Allow user to edit reipes
  - [ ] Produce success/error messages when the user interacts.
- [ ] View view
  - [ ] Exist
  - [ ] Load data based on what was clicked
  - [ ] Checkbox for ingredients
  - [ ] Checkbox for prep instructions
  - [ ] Allow user to return to Browse
- [ ] Search view
  - [ ] Exist
  - [ ] Load data based on search
  - [ ] Delete and save after confirmation
  - [ ] Allow you to go into edit/view mode on a recipe
  - [ ] Allow user to return to browse
