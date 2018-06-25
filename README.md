# WindowScript

WindowScript is a markup language, that allows you to create a window on a very simple way with HTML content.
The core is written in [java](https://www.java.com), so you can use it via command line on every os, but there are launcher and an installer for Windows.

## Documentation

The format is very simple, each line has to start with a single keyword and after it there will be the arguments for it.
The interpreter is not case-sensitive. If an command is used more than once, the last value will be used (Except of child command). 
It doesn't matter in what order the commands are.  
Following keywords are implemented:

### title

>Sets the title of the Window. If the HTML content defines a title, the content title will be prefered. If neither, the name of the file will be the title.
>Everything is allowed as argument.

### width
>Sets the with of the window.
>Single doubles (e.g. 400 or 333.333) are allowed as argument.
>If the argument is negative, zero will be set.

### hight

>Sets the height of the window.
>Single doubles (e.g. 400 or 333.333) are allowed as argument.
>If the argument is negative, zero will be set.

### xpos

>Sets the x position of the window.
>Single doubles (e.g. 400 or 333.333) are allowed as argument.
>If the argument is negative, zero will be set, if the agument is heigher than the screen width, the screen width will be used.

### ypos

>Sets the y position of the window.
>Single doubles (e.g. 400 or 333.333) are allowed as argument.
>If the argument is negative, zero will be set, if the agument is heigher than the screen height, the screen height will be used.

### mode

>Sets the mode, with that the window is shown.  
>Following arguments are allowed:
>
>* minimized: iconifies the window.
>* normal: shows the window normaly.
>* fullscreen: shows the window in fullscreen.
>* maximized: maximizes the window. If supported, the taskbar and the title bar will disapear.

### pos

>Sets the pos (the xpos and the ypos) of a window relative to the screen size and size of the window.  
>Following arguments are allowed:
>
>* left_up: the upper left corner of the window will be in the upper left corner of the screen.
>* left: the window will be on the centre line of the screen. The top of the window will be at the top of the screen.
>* left_down: the bottom left corner of the window will be at the bottom left corner of the screen.
>* centre_up: the window will be in the middle of the screen. The top of the window will be at the top of the screen.
>* centre: the window will be on the centre line and on the middle of the screen. Default.
>* centre_down: the window will be on the middle of the screen. The bottom of the window will be at the bottom of the screen.
>* right_up: the upper right corner of the window will be at the upper right corner of the screen.
>* right: the window will be on the centr line of the screen. The right side of the window will be at the right side of the screen.
>* right_down: the bottom right corner of the window will be at the bottom right corner of the screen.

### resizable

>Sets if the window will be resizable. This means that the size cannot be changed and the window can't get fullscreen or maximized.
>Allowed arguments are true (default) and false.

### minwidth

>Sets the minimum with for the window. This means that the user cant make the window smaller.
>The width command isn't affected by it.
>Single doubles (e.g. 400 or 333.333) are allowed as argument.
>If the argument is negative, the window won't have a minwidth (default).

### maxwidth

>Sets the maximum with for the window. This means that the user cant make the window bigger.
>The width command isn't affected by it.
>Single doubles (e.g. 400 or 333.333) are allowed as argument.
>If the argument is negative, the window won't have a maxwidth (default).

### minheight

>Sets the minimum height for the window. This means that the user cant make the window smaller.
>The height command isn't affected by it.
>Single doubles (e.g. 400 or 333.333) are allowed as argument.
>If the argument is negative, the window won't have a minheight (default).

### maxheight

>Sets the maximum height for the window. This means that the user cant make the window bigger.
>The height command isn't affected by it.
>Single doubles (e.g. 400 or 333.333) are allowed as argument.
>If the argument is negative, the window won't have a maxheight (default).

### opacity

>Sets the opacity for the window. This means, if it is supported by the os, the window is getting transparent.
>Allowed argument is a float between 0.0 (invisible) and 1.0 (default)

### style

>Sets the style for the window. This will look like the os implementation for it.
>Allowed arguments are:
>
>* decorated: The window is shown with a title bar, a minimize, maximize and a close button (default).
>* transparent: The windows title bar will be invisible, the window still can be clossed over the taskbar.
>* undecorated: The windows title bar will be removed, the window still can be closed over the taskbar.
>* unified: The windows title bar will get the same color like the background of the window.
>* utitlity: The window will have only a close button. It also won't be shown in the taskbar.

### icon

>Sets the icon for the window, which will be shown in the title bar and in the taskbar.
>The argument must be a path to an image in one of the following formats:
>
>* png
>* jpg
>* bmp
>* ico
>
>If the path is absolute, the path won't get modified, otherwise the parent folder of the WindowScript file will be the root.

### content

>Sets the content, that will be shown in the window. It is implemented with [javafx.scene.web.WebView](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/web/WebView.html), which underlying engine is [WebKit](https://webkit.org/).
>Usually the content is a HTML/Text file, given as a path. If the path is absolute, the path won't get modified, otherwise the parent folder of the WindowScript file will be the root.

### child

>Adds a child window to the actual window. There can be more than one child command for having multiple child windows.
>Child window means, that the window is showing as an own window, but the icon will be at one slot in the taskbar. If the parent window has a setted icon and the child not, the child will inherit the parent one. Same thing with the style. A specific behaviour can be set with the modality command.
>The argument must be a path  to another WindowScript file. If the path is absolute, the path won't get modified, otherwise the parent folder of the WindowScript file will be the root.

### modality

>Sets the modality of a window. If the window is the main window, this will have no effect.
>Allowed arguments are:
>
>* none: The window is shown as an own, independend window, and will have no effect to its parent (default).
>* window: The window will block all events to his underlying windows, so it has to be closed first.
>* application: The window will block all events until it is closed.

## Example

Following file will shown as a window, with:

* the width 800
* the height 400
* the content from the file Content.html, which is next to the WindowScript file
* resizable on true
* the title "My Window"
* the icon of the png Icon.png, which is next to the WindowScript file
* the position in the centre of the screen

    ````text
    title My Window  
    width 800  
    height 400  
    resizable true  
    icon Icon.png  
    content Content.html
    pos centre

resizable and pos are optional, because they are already default.

## License

The project is under the [GNU GENERAL PUBLIC LICENSE 3](https://www.gnu.org/licenses/gpl-3.0) and makes use of following third-party projects:

* [java](https://www.java.com)

## Contact

* [GitHub](https://github.com/Steinschnueffler)
* [E-Mail](mailto:Linus@Dierheimer.de)

~ Linus Dierheimer