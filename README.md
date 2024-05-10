# Historical Landmark Donation Application for Android

## Functionality Discussion

The goal of the application is to allow the users to donate to Historical Landmarks, the donation types are either through direct or paypal payments.

### User interaction with the application

The user begins their interaction with the application by landing on the login screen. The user has a choice of logging in with an established account they made previously
or to create a new one by either directly creating a new account or using Google Sign-in. After that the user lands on the report view where you can see the donations made,
on this view if any donations already exist the user can see their own ones but if they toggle the switch at the header they are able to view other user donations as well.
On the header as well there is a search bar allowing the user to search for donations by amount, email and payment method. The user can swipe left to delete a donation on this
view or swipe right or tap on the donation to edit it. From the header the user can also open a navigation menu to use to visit other views of the application such as donation
which brings the user to the donation view and allows them to create a new donation. First thing that is displayed is a welcome (user) message that dynamically checks for which
user is logged in currently and displays their email next to the welcome message. This view also consists of a scrolling amount picker that a user can use to quickly input the
amount they wish to donate or they can manually input the value into the amount field. A user can choose either direct or paypal payment type. A user can also write a message
and to also dedicate the donation to a cause by filling the relevant field for it. The donation view also displays the Total amount so far and a progress bar that fills up when
donating. Using the navigation menu again the user can visit the about section of the application that just displays a message what the application is about and displays a little
knight image to accompany the theme of the application. Going back to the navigation menu the user can also visit the location view which is a google maps integration with the
application that lets the user see where the donations come from and displays them on the map, the switch filter to show all donations also works here. The navigation menu features
all the necessary connection to these views but also features the option to sign out of the account which returns the user to the log in screen, the navigation menu also lets the user
see their profile details such as email and profile picture on the header of the navigation menu. Tapping the profile picture lets the user change their profile picture from the devices
storage, the profile picture persists thanks to firebase storage integration.

## Third Party / Google APIs used

### Firebase
The application uses firebase for login and sign up authorization to allow users to be stored on the firebase. Firebase is also used for a realtime database to store the donations and to
retrieve them whenever a user logs into the application. Firebase is also used for storage of user images for their profile pictures.

- Using Google sign-in or email and password credentials, users can securely access app features and register using Firebase Authentication for user authentication.
- A cloud-hosted NoSQL database called Firebase Realtime Database is used to store and synchronize app data in real time between users' devices. This makes it possible
to store and retrieve donation-related data—such as donor profiles, donation specifics, and user-specific information—efficiently.

Using Firebase simplifies backend development tasks for user management and data storage and synchronization and allows the developer to focus on building the core app
functionality.

### Picasso 
Picasso is an effective Android library for downloading and caching images. With features like caching, placeholder images, error handling, and more, it makes loading
images from URLs or local resources into ImageViews easier.

The app uses Picasso to load profile pictures of users efficiently into ImageViews displayed in the donation cards.

### Timber for Logging
Timber is a logging library for android that provides a simple and extensible API for logging messages in the app.
Timber was used here to debug messages, warnings, and errors during the development and testing of the application.

### Retrofit for networking
Retrofit is a Java and Android type-safe HTTP client. By translating API endpoints and responses into Java interfaces
and objects, it makes the process of sending network requests to a RESTful API easier.

Using Retrofit in the application to communicate with a backend server or API (in the case of the application the donationService) to perform CRUD (Create, Read, Update, Delete)
operations on the donation data. Retrofit handles the HTTP requests and responses, making network communications more manageable and efficient.

### Google Maps for Locations
Google maps SDK for android allows the integration of Google Maps functionality into the application, including displaying maps, adding markers, handling user interactions,
and accessing location-based service. Google maps is used in this application to display the donations made by users via location markers.

## UML

UML: (https://drive.google.com/file/d/1VkqJsZp2leXt8ya-aO11-_wkCVenqoAE/view?usp=sharing)

Application: (https://drive.google.com/file/d/16QhhtjDgHBMWXBlTIRGJlSGRQfLgaEC3/view?usp=sharing)

## UX/DX Approach

The UX/DX approach taken was trying to closely follow the android studio general practice for the UX/DX design, the goal was to make the application as easy to use without
any requirement of guides and frustration of finding a feature within the application. The placement of text fields and buttons etc was placed in a way for ease of use and
to not have the user be able to accidentally hit the wrong field etc. Placement of necessary fields that the user had to view was approached so that other buttons or fields do
not overlap and therefore the user can clearly see the necessary information that the user has to see.

For the application the MVVM design was picked as:

### Separation of Concerns:
- MVVM encourages a distinct division of responsibilities among the app's three main components: the Model, View, and ViewModel.
- The application's data and business logic are represented by the model. In your instance, it consists of Firebase database management
in FirebaseDBManager and data models like DonationModel.
- The View is a representation of the app's user interface elements, including layouts, fragments, and activities. The View layer in your
app is made up of activities and fragments that show information about donations and user interactions.
- The ViewModel functions as a bridge connecting the Model and the View. It takes information out of the Model and gets it ready to show in the View.
The DonationViewModel in your application may manage donation-related data operations, including retrieving donation lists, interpreting user input, and adjusting the user interface (UI).

### Testability:
- MVVM architecture facilitates unit testing and improves the testability of your app's components.
- ViewModels do not require Android-specific dependencies to be tested using unit tests because they are not dependent on the Android framework. This makes it possible to test business logic
and data manipulation in greater detail.
- It is simpler to create separate unit tests for each component when the View layer is separated from the ViewModel and Model layers, which results in more dependable and manageable code.

### Data Binding and LiveData Integration:
- MVVM pairs well with Android Jetpack components such as Data Binding and LiveData, enhancing the development experience and simplifying UI updates.
- Data Binding allows to bind UI components directly to ViewModel properties, eliminating the need for manual findViewById() calls and reducing boilerplate code.
- A reactive and effective way to propagate changes from the ViewModel to the View is to use LiveData, a lifecycle-aware observable data holder class. LiveData makes sure that user interface elements
automatically update when underlying data changes, giving users a more consistent and responsive experience.

### Scalability and Maintainability:
- MVVM promotes a modular and scalable architecture that supports the growth and evolution of your app over time.
- It is simpler to implement new features, refactor existing code, and make changes without inadvertently causing side effects when concerns are clearly segregated and clear communication channels are
established between components.
- The MVVM architecture fosters code reusability and maintainability, allowing developers to build upon a solid foundation and iterate on app functionality iteratively.

## Git Approach
The Git workflow I used was using branches when adding new features to the application and then using pull requests into the main branch on Github once everything was checked
and found working. After sufficient features were added tagged release/s were created for the project to signify a milestone worthy of being a release candidate.

Using the branching model it let me work on features separately and in an isolated area where it would let me go back to the previous version easily and start work on something else
if I had to scrap a feature etc.

(https://github.com/maciejmarchel12/mobileapp2assignment2/tree/1.0)

Using Github allows for easy collaboration between people where those who have access can make their own changes and then have them merged to the main build, Github also allows for forking
of projects so that someone can grab the project and continue work on their own version/application based on the one that was forked. Github is also great for code reviews and you can easily
check to see what was changed between each commits and branches etc.

## Personal Statement:
Working on this project I learned new things from it, such as learning how to integrate the firebase authentication and storage and working with the MVVM architecture.

For challenges during the development of the project I faced an issue of integrating google sign-in with the application where the linked lab guide wasn't the correct URL
as perhaps google updated their documents and feature, however I did find a workaround of doing the steps in reverse and using firebase to apply the SSH key and have it
connect to the Google Console for developers. Another issue I faced was working with the MVVM architecture and trying to sometimes figure out why a feature wasn't being
implemented correctly but the solution to this was using timber for logging and debugging the features I was working on and finding the solution that way.

Working on this project has been a valuable learning experience that has expanded my knowledge on new features and architectures, I have also expanded my proficiency in 
using popular Android development tools and libraries such as LiveData, Data Binding, and Timber to streamline development workflows and to enhance code quality.

## Refrences:
1. Google Maps SDK for Android: Google Developers Documentation: (https://developers.google.com/maps/documentation/android-sdk/intro)
2. Firebase Authentication, Storage, and Realtime Database: Firebase Documentation:
    - (https://firebase.google.com/docs/auth)
    - (https://firebase.google.com/docs/database)
    - (https://firebase.google.com/docs/storage)
3. Retrofit: Retrofit Documentation: (https://square.github.io/retrofit/)
4. Timber: Timber Documentation: (https://github.com/JakeWharton/timber)
5. Picasso: Picasso Documentation: (https://square.github.io/picasso/)
6. MVVM Architecture:
   - Android Developers Guide: (https://developer.android.com/jetpack/guide)
   - Android Architecture Components Documentation: (https://developer.android.com/topic/libraries/architecture/viewmodel)