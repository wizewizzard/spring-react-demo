# Spring and React cats 
## Overview
### Tech stack
**Client:** React

**Server:** Spring boot, Hibernate
### What it is?
This is a single-paged-application driven by the spring framework on the backend and the React.js on the frontend.
A basic post-comment domain is used. No authentication and authorization is involved. Just wanted to test out 
how spring and react combination works. Here is a couple of screenshots what it looks like.

[posts listing page](./img1.png)

[the post page](./img2.png)

[comment section](./img3.png)
#### Why no authentication used? 
Mainly because it was not the point of the study, and secondly I don't yet know React.js good enough to perform that.
Certainly this is going to be one of the points I will address in another studies I will do later.
#### Database?!
An inmemory H2 database is used.
### Challenges I encountered
#### Building the application
I was following the [guide ](https://github.com/kantega/react-and-spring/blob/master/README.md).
After some minor tweaking frontend and backend became friends and were packed into a single .jar, but one little thing did not work properly: **the routing**.
#### Routing requests to react app
When a user starts his journey from the root i.e. ```http://localhost:8080``` everything works just fine.
But when a user enters, let's say ```http://localhost:8080/posts/1```, the spring does not dispatch the request to the React application.
The spring backend hopelessly tries to process the request on its own and fails. To direct the request to ```index.html``` either a filter or a controller can be used.
I've chosen the controller option:
```
@Controller
public class ReactAppController {
    //Request that doesn't start with *api* will be routed to index.html
    @RequestMapping(value = { "/", "/{x:[\\w\\-]+}", "/{x:^(?!api$).*$}/*/{y:[\\w\\-]+}","/error"  })
    public String getIndex(HttpServletRequest request) {
        return "/index.html";
    }
}
```
For more information about this topic look into the given [stackoverflow thread](https://stackoverflow.com/a/61589443/18308068).

#### Validation
Yes, I know that using domain models as DTOs is not a good practice as it may expose unnecessary details about entity or 
transfer redundant data. But to save some time I've made this decision.
``@RestControllerAdvice`` handles exceptions services or controllers fire. Among those exceptions ``MethodArgumentNotValidException``
can be handled. Handler converts validation errors into a map, where key is name of the field and values is a list of violations for this field. 
And responds with a `BAD_REQUEST` and marshalled errors.

React application receives the `BAD_REQUEST` code, uses hook to update component's state 
and renders violations above fields where the mistake was made with ``<Errors ... />`` component. Server errors
are processed the same way.

#### Image's lifecycle
When user submits a form for a post creation/update an image are sent along with json data. That combination forms a `multipart/form-data` 
request body. And to achieve that in React app this kind of code is used:
```
        const json = JSON.stringify(post);
        const postBlob = new Blob([json], {
          type: 'application/json'
        });
    
        formData.append('post', postBlob);
        formData.append('image', image);
```

The Spring framework allows to retrieve parts of the request with `@RequestPart` annotation. Later the image is
scaled down to create a thumbnail and both (original and thumbnail) will be saved into the unique directory. Thanks to 
the Id that is given to a post entity by database sequence the directory is unique for sure.

When post is updated or deleted old images and directory (in case of deletion) will be deleted as well.

#### HATEOAS
I did not use the potential `spring-boot-starter-hateoas` provides in the current project. I used it's model assemblers
as dto convertors, added some links (that are not used anywhere else :) ), and nothing more. 
Just wanted to have a little practice with it.

## To run
Docker is required.

`cd` into the directory where the project was cloned and run
`docker build -t react-cats .` to build an image and `docker run -p <port>:8080 react-cats` to start the app.
The ```./stubs``` directory has some fake data prepared. 
