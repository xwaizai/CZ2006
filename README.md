# CZ2006 Project (EZMeet)
https://user-images.githubusercontent.com/13762085/144347349-7bc12695-7a1d-45f9-9a34-618e1afff405.mp4

Below will be solely focus on documentation of this project and features codes that make this possible

# Documentation of the use case diagrams (Except for Class Diagram)
![4  Use Case Diagram](https://user-images.githubusercontent.com/13762085/144356308-b7b3b4e8-3876-4e95-a2b5-ec6f7909023d.png)

![5 1 Initial Loading Sequence Diagram](https://user-images.githubusercontent.com/13762085/144356329-6049b2db-86a3-4ac5-ab66-0f9fe6c9b4cb.png)

![5 2 Main Sequence Diagram](https://user-images.githubusercontent.com/13762085/144356331-527173e2-e10a-46d3-b7d4-cc20ee4cba24.png)

![5 3 Postal Validation Sequence Diagram](https://user-images.githubusercontent.com/13762085/144356339-ca9bbf1c-7d26-4964-af8f-c6ee97c61ad4.png)

![5 4 Places Sequences Diagram](https://user-images.githubusercontent.com/13762085/144356344-6659a137-34e3-4956-be26-d015d62bc191.png)

![5 5 Bus Arrival Sequence Diagram](https://user-images.githubusercontent.com/13762085/144356354-2bb57bcc-262f-436c-84b2-0ab80584cfd7.png)

![5 6 Train Service Alert Sequence Diagram](https://user-images.githubusercontent.com/13762085/144356360-c6928904-f1da-48f7-a62d-f7744cfbbc17.png)

![5 7 Traffic Incidents Sequence Diagram](https://user-images.githubusercontent.com/13762085/144356367-ef247462-7d81-4571-8289-0114a0825347.png)

# Dialog Map
![6  Dialog Map](https://user-images.githubusercontent.com/13762085/144356374-bb1ddcf5-3819-403a-80e6-9a86a52b1fdd.png)

![7  System Architecture](https://user-images.githubusercontent.com/13762085/144356382-08172c01-10d9-4f5b-9135-32c0bfeca949.png)

# How to scrape data from MOH website

First, the link to the MOH website needs to be established such as:

```
"https://www.moh.gov.sg/news-highlights/details/update-on-local-covid-19-situation-(28-oct-2021)"
```
Using JSoup to scrap the HTML attributes in a JSON format and scraping the cluster information from the table attribute. Then create a  thread for each active cluster in order to get the postal code's information through [Google Places API] (https://developers.google.com/maps/documentation/places/web-service/overview).
```
for (Element tablerow : tableRows)
{
    Elements tableDatas = tablerow.getElementsByTag("td");
    // we have to skip anything that has the word case! so that we dont send too many requests
    String dataStr = tableDatas.get(0).text();
    Matcher m = pattern.matcher(dataStr);
    if (m.find() == true)
    {
        continue;
    }
    Thread googleAPIPlace = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    GoogleMapsAPI.getPlaceInfo(dataStr, fields, clusterLocations);
                }
            }
    );
    googleAPIPlace.run();
    listOfCreatedThreads.add(googleAPIPlace);
}
```
This is computationally expensive thus even this loop is run in a separate thread so that it is not blocking in the main thread.

The issue will be after waiting for all the threads to join, how do we display the cluster UI back on your phone? It is as simple as having another thread to observe whether the loop is finished then it gets the main activity to run a specific codes on the UI thread.
```
Activity myActivity = getActivity();
m_COVIDInfoBox = getLayoutInflater().inflate(R.layout.covid_infobox, null);
new Thread() {
    public void run()
    {
        while (!m_WebScrapper.getIsDone() || m_GMap == null);
        myActivity.runOnUiThread(() -> {
            setMarkers();
        });
    }
}.start();
```
# Navigation and drawing the lines on the map
Getting navigation between 2 places from the Google Map is difficult and drawing the polylines can even more difficult as developers will have to slowly iterate through the list of coordinates to draw individual lines. Thus using a [3rd party library](https://github.com/akexorcist/GoogleDirectionLibrary.git) helps the development of this feature significantly!
```
GoogleDirection.withServerKey("YOUR GOOGLE API KEY")
        .from(new LatLng(GlobalHolder.getInstance().getStart().m_Lat, GlobalHolder.getInstance().getStart().m_Long))
        .to(new LatLng(GlobalHolder.getInstance().getDesination().m_Lat, GlobalHolder.getInstance().getDesination().m_Long))
        .transportMode(TransportMode.DRIVING)
        .execute();
```
Using these simplified functions, we are able to easily derive different transport modes for the same route. 
