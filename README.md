# twitter-rest-api

Run the application : mvn spring-boot:run

In browser : http://localhost:8082/
 
APIs :

1. /username - Returns name of logged in user
Tests :
    1. /username - It should return logged in username only


2. /login - Login using LDAP username and password
Tests:
    1. Login with Username : Bob and Password : Bob. It should be successful
    2. Login with Username : Bob and Password : Bob3. It should not be successful
        "Bad credentials" error
    3. Login with Username : Boby and Password : Bob. It should be successful
        "Bad credentials" error
    4. Login with Username : Boby and Password : <Empty string>. It should be successful
            "Empty password" error
    5. Login with Username : <Empty string> and Password : Bob. It should be successful
            "Empty username" error
            

3. /logout - Log out current user
Tests:
    1. It should logout current user

4. /api/follow - Follow another user
Usage : /api/follow?followeeId=<another-username>
Tests:
    1. /api/follow?followeeId=<Empty string>
        "Required String parameter 'followeeId' is not present" error
    2. /api/follow?followeeId=<Non existing user name(Samy)>
        "User : Samy does not exist in DB" error
    3. /api/follow?followeeId=Sam
        "Connection saved" message first time
    4. /api/follow?followeeId=Sam
        "Connection for bob and Sam exists in DB" message for next tries


5. /api/tweet - Create new tweet
Usage : /api/tweet?tweetText=<tweet-content>
Tests:
    1. /api/tweet?tweetText=<Empty string>
        "Tweet content cannot be empty" error
    2. /api/tweet?tweetText=<string with length more than 255>
        "Tweet content cannot more than 255 characters" error
    3. /api/tweet?tweetText=<Some stringg>
        "Tweet saved" message

6. /api/feed - See the wall with all tweets by current user and the users that 
he is following
Usage : 
i. /api/feed - Displays all tweets
Tests:
    1. /api/feed - Displays all tweets by user and other users that he is follwoing
    2. /api/feed?page=0&size=5
        Display first page with 5 tweets on each page

