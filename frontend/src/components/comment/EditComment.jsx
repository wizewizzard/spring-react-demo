import React, { useState } from 'react'
import Errors from '../Errors';
import CommentService from '../../service/CommentService';
import ServerError from '../util/ServerError';

export default function EditComment(props) {
    const {postId, onUpdate} = props;
    const [author, setAuthor] = useState('');
    const [content, setContent] = useState('');
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const response = await CommentService.publishComment(postId, {author, content});
        if(response.status >= 200 && response.status < 300){
            clearState();
            onUpdate();
        }
        else if(response.status >= 400 && response.status < 500){
            const data = await response.json();
            setErrors(data);
        }
        else{
            setServerError(true);
        }
    }

    const clearState = () => {
        setAuthor('');
        setContent('');
        setErrors({});
    }

    const renderComponent = () => {
        return (
            <div className='card'>
                    <div className='card-body'>
                        <div className='fs-4 my-2'>Add your comment</div>
                        <form onSubmit={handleSubmit}>
                            <div className='mb-3'>
                                <label className='form-label fs-5'>Nickname</label>
                                {errors.hasOwnProperty('author') &&
                                    <Errors errors={errors.author} />
                                }
                                <input placeholder='Nickname' name='author' className='form-control' 
                                value={author} onChange={e => setAuthor(e.target.value)} />
                            </div>
                            <div  className='mb-3'>
                                <label className='form-label fs-5'>Your comment</label>
                                {errors.hasOwnProperty('content') &&
                                    <Errors errors={errors.content} />
                                }
                                <input placeholder='Comment' name='content' className='form-control' 
                                value={content} onChange={e => setContent(e.target.value)} />
                            </div>
                            <div><button className='btn btn-outline-success'>Publish</button></div>
                        </form>
                    </div>
                </div>
        );
    }

    return serverError ? <ServerError message={'Server error occured. Unable to load comment.'}/> : renderComponent();
}
