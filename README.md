PhunHomework
============

Screenshots

![Alt text](/screenshots/device-2014-05-15-031344.png?raw=true "")
![Alt text](/screenshots/device-2014-05-15-031443.png?raw=true "")
![Alt text](/screenshots/device-2014-05-15-031457.png?raw=true "")
![Alt text](/screenshots/device-2014-05-15-031511.png?raw=true "")
![Alt text](/screenshots/device-2014-05-15-031550.png?raw=true "")
![Alt text](/screenshots/device-2014-05-15-031557.png?raw=true "")
![Alt text](/screenshots/device-2014-05-15-031627.png?raw=true "")

Highlights:

1. Stateful persistent fragments: each fragment's instance is persistent and its UI is changed accodingly, even across orientation changes. In VenueListFragment particularly, the current scroll position of the list is maintained, so that the list does not scroll to the top even if the fragment gets detached and reattached on orientation change/fragment swap. The current networking status (fetching, successful, error) is maintained and reflected in the UI accordingly. VenueDetailListFragment renders its UI and shows its ActionItem only when a venue item is passed to it.
2. ActionBarSherlock, together with HoloEverywhere have been used to maintain backwards compatibility with Gingerbread not only in the use of the ActionBar and the share functionality, but in the use of the Holo theme as well. Standard Android layouts and styles have been used in the adapter and the detail fragment as well, to be as close to the holo theme as possible.
3. Instead of one big package (this being a relatively small project), classes are organized in packages based on their logical grouping for future expansion. Some base classes that I use as a standard in my other projects have been copied over and utilized as well. 
4. For code simplicity, some 3rd party libraries have been used, namely: AQuery for chain-style setting up of views and async image downloading, and Jackson for easy string-to-pojo transforation. SuperToasts were used for user notification as well.
