import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import PostService from '../../service/PostService';
import Errors from '../Errors';
import Loader from '../Loader';
import ServerError from '../util/ServerError';

export default function EditPost() {
    const { postId } = useParams();
    const navigate = useNavigate();
    const [title, setTitle] = useState('');
    const [brief, setBrief] = useState('');
    const [image, setImage] = useState(null);
    const [previousImage, setPreviousImage] = useState('');
    const [content, setContent] = useState("");
    const [errors, setErrors] = useState({});
    const [loaded, setLoaded] = useState(false);
    const [serverError, setServerError] = useState(false);
    
    const onChangeFile = (event) => {
        setImage(event.target.files[0]);

   }

    useEffect(() => {
         if(postId != null){
            setLoaded(false);
            fetchPostToEdit();
         }      
         else{
            clearState();
            setLoaded(true);
         }
    }, [postId])
    
    const fetchPostToEdit = async () => {
      const response = await PostService.getPost(postId);
      if(response.status >= 200 && response.status < 300){
        
        const post = await response.json();
        setTitle(post.title);
        setBrief(post.brief);
        setContent(post.content);
        setPreviousImage(post.imagePath);
        setLoaded(true);
      }
      else  if(response.status >= 400 && response.status < 500){
        navigate('/404');
      }
      else{
        setServerError(true);
      }      
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        const formData = new FormData();
        const post = {
            title,
            brief,
            content
        };
        const json = JSON.stringify(post);
        const postBlob = new Blob([json], {
          type: 'application/json'
        });
    
        formData.append('post', postBlob);
        formData.append('image', image);
        let method;

        if(postId == null){
          method = () =>  PostService.createPost(formData);
        }
        else{
          method = () =>  PostService.updatePost(postId, formData);
        }

        method()
          .then(response => {
            if(response.status >= 200 && response.status < 300){
              response.json()
              .then(data => {navigate(`/posts/${data.id}`)})
            }
            else if(response.status >= 400 && response.status < 500){
              response.json().then(errs => {setErrors(errs);}); 
            }
            else{
              setErrors({server: ['Server did not respond']});
            }
          });
      }

    const handleCancel = () => {
        navigate('/posts');
    }

    const clearState = () => {
      setTitle('');
      setBrief('');
      setContent('');
      setImage(null);
      setErrors({});
      setPreviousImage('');
      setServerError(false);
    }

    const renderComponent = () => {
      return (
        <div className='container'>
          <div className='row justify-content-center'>
            <div className='col-6 card'>
              <h3 className='text-center mt-3'>Create post</h3>
              <div className='card-body'>
                <form onSubmit={handleSubmit}>
                {errors.hasOwnProperty('server') &&
                      <Errors errors={errors.server} />
                    }
                  <div  className='mb-3'>
                    <label className='form-label fs-4' >Title</label>
                    {errors.hasOwnProperty('title') &&
                      <Errors errors={errors.title} />
                    }
                    <input placeholder='Title' name='title' className='form-control' 
                    value={title} onChange={e => setTitle(e.target.value)} />
                  </div>
                  <div  className='mb-3'>
                    <label className='form-label fs-4'>Brief</label>
                    {errors.hasOwnProperty('brief') &&
                      <Errors errors={errors.brief} />
                    }
                    <input placeholder='Brief' name='brief' className='form-control' 
                    value={brief} onChange={e => setBrief(e.target.value)} />
                  </div>
                  <div  className='mb-3'>
                    <label className='form-label fs-4'>Content</label>
                    {errors.hasOwnProperty('content') &&
                      <Errors errors={errors.content} />
                    }
                    <textarea placeholder='Content' name='content' className='form-control' 
                    value={content} onChange={e => setContent(e.target.value)} rows="20" cols="50"/>
                  </div>
                  <div  className='mb-3'>
                    <label className='form-label fs-4'>Image</label>
                    {errors.hasOwnProperty('image') &&
                      <Errors errors={errors.image} />
                    }
                    {
                    previousImage != '' && 
                    (<div className='my-2'>
                      <div className='text-muted small'>
                        Previous image
                      </div>
                      <div className='col-5 row'>
                        <img className='scaled' src={`/${previousImage}`} alt='Post image' />
                      </div>
                    </div>) 
                    }
                    <input type='file'  accept="image/png, image/gif, image/jpeg" name='image' 
                    className='form-control' onChange={onChangeFile} />
                  </div>
                  <button type="sumbit" className='btn btn-outline-success' 
                  onClick={handleSubmit}>Publish</button>
                  <button type="button" className='btn btn-outline-danger' 
                  onClick={() => handleCancel()}>Cancel</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      );
    }
    return serverError ? <ServerError /> : loaded ? renderComponent() : <Loader />;
}
