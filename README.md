## Engineering Lab Project
Merchant can create a new shop by filling in a form containing: the name of the shop, the categories/tags relevant to the           shop, and other fields that are up to you. The Merchant can upload products to populate the shop along with a description, picture, and inventory number. Customers can find a shop by looking up the name of the shop or searching by category/tag. Once they find the shop the want, they can browse through the product catalog of the shop, and can then decide to purchase one or many products by putting them in the Shopping Cart and proceeding to Checkout. The purchase itself will obviously be simulated, but purchases cannot exceed the inventory.

 ### Added Technology
 FaaS, Microservices, Serverless: Amazon Lambda, Google Cloud Functions, Apache OpenWhisk...
 
 ### Important Links
 Travis CI: https://travis-ci.org/haseebakhan10/EngLabProject <br />
 Heroku: https://dashboard.heroku.com/apps/minishopifygroup5
 
 ### To Test The Application
Test Locally:
 1. Clone the repo
 2. Open "src/main/java/ShopifyProj/Launcher.java" and run the Launcher
 3. Go to http://localhost:8181/ <br />
 
Alternatively, use Heroku:
 1. Go to https://dashboard.heroku.com/apps/minishopifygroup5
 2. Click "Open App" <br />
 
To Test:
 1. Use the navigation menu or buttons to test the app (NOTE: some pages ask for phone numbers, email, or credit card numbers. Nothing is done with this information, so you can just enter fake info)
  
  
 ### Current State
 - Users can signup as customer and/or merchants
 - Users can login as customer and/or merchants 
 - Shops can be created
 - Merchants can modify shops by changing the shops name, adding/removing tags, and adding/removing/editing items 
 - Shops can be searched by using the name or tags
 - Shops have different views for customers and merchants
 - Customers can add items to cart and checkout 
 - Setup and integrated Firebase/database
 - App is running on Travis CI and Heroku
 
 
 ### UML Diagram
 NOTE: We used Strings for the IDs of our classes. The reason we did this as opposed to the autogenerated IDs that Spring provides is because we wanted it to match the IDs in the database. As part of our project, we had to use Firebase. Firebase uses alphanumeric values for its IDs, so we had to use Strings to maintain references to these IDs in the corresponding objects.
 ![UML](https://github.com/haseebakhan10/EngLabProject/blob/master/diagrams/Eng%20Lab%20Project%20UML.png)
 
 ### Database Schema
 NOTE: As part of the project we have to use Firebase, which is NoSQL, so the database schema follows NoSQL
 ![Database Schema](https://github.com/haseebakhan10/EngLabProject/blob/master/diagrams/Database%20Schema.png)
 
 ### Plan For Next Sprint
 - Project Complete
