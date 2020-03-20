## Engineering Lab Project
Merchant can create a new shop by filling in a form containing: the name of the shop, the categories/tags relevant to the           shop, and other fields that are up to you. The Merchant can upload products to populate the shop along with a description, picture, and inventory number. Customers can find a shop by looking up the name of the shop or searching by category/tag. Once they find the shop the want, they can browse through the product catalog of the shop, and can then decide to purchase one or many products by putting them in the Shopping Cart and proceeding to Checkout. The purchase itself will obviously be simulated, but purchases cannot exceed the inventory.

 ### Added Technology
 FaaS, Microservices, Serverless: Amazon Lambda, Google Cloud Functions, Apache OpenWhisk...
 
 ### Important Links
 Travis CI: https://travis-ci.org/haseebakhan10/EngLabProject <br />
 Heroku: https://dashboard.heroku.com/apps/minishopifygroup5
 
 ### To Test The Application
 1. Clone the repo
 2. Open "src/main/java/ShopifyProj/Launcher.java" and run the Launcher
 3. Go to http://localhost:8181/
 4. Use the navigation menu or buttons to test the app (NOTE: some pages ask for phone numbers, email, or credit card numbers. Nothing is done with this information, so you can just enter fake info)
  
  
 ### Current State
 - Shops can be created
 - Shops can be searched by using the name or tags
 - Merchants can modify shops by chaning the shops name, adding/removing tags, and adding/removing items
 - Shops have different views for customers and merchants
 - App is running on Travis CI and Heroku
 - Setup and integrated Firebase/database
 - Setup account signup for merchant and customers
 - Customers can add items to cart and checkout
 
 ### UML Diagram
 ![UML](https://github.com/haseebakhan10/EngLabProject/blob/master/diagrams/Eng%20Lab%20Project%20UML.png)
 
 ### Database Schema
 NOTE: As part of the project we have to use Firebase, which is NoSQL, so the database schema follows NoSQL
 ![Database Schema](https://github.com/haseebakhan10/EngLabProject/blob/master/diagrams/Database%20Schema.png)
 
 ### Plan For Next Sprint
 - Add login support for customers and merchants
 - Improve testing for cart and checkout
 - Update the user interface
 - Update database to account for merchant's for owning shops
 - More updates for merchant shop view
