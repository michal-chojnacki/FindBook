# FindBook
![](https://circleci.com/gh/michal-chojnacki/FindBook.svg?style=shield&circle-token=:circle-token)
![](https://app.bitrise.io/app/bff3693adedc20f9/status.svg?token=t35OQI9ZIXtf9iFTJxh27w&branch=master)

The app uses camera for scanning book titles and looks for them using Goodreads API. It enables in easy way find such data about books like years of publish, comments or ratings.

##Google Play
App is available here: <https://play.google.com/store/apps/details?id=com.github.michalchojnacki.findbook>

##Architecture:
App uses clean architecture approach with DI impemented with Dagger. Approach for using Dagger is highly inspired with [this presentation](https://www.youtube.com/watch?v=9fn5s8_CYJI). 

Besides of that huge inspiration for architecture of the app is [Plaid](https://github.com/android/plaid).

For camera implementation it used some code from <https://github.com/googlesamples/android-vision>, adjusted to requirements of OCR scanner used in the app

For background thread tasks it uses coroutines.

##Other remarks:
The app is very simple, but I think there is enough code to show my current approach how I like to structure the app using clean architecture, Dagger and coroutines. The code is generally quite well tested, it lacks only tests for view models, which shouldn't be difficult to write. However its structure enables in easy way adding new features.