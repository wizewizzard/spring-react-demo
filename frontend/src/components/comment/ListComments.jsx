import React, { useEffect, useState } from 'react'
import CommentService from '../../service/CommentService';
import Loader from '../Loader';
import PageRow from '../util/PageRow';
import ServerError from '../util/ServerError';
import EditComment from './EditComment';
import { Link } from 'react-router-dom';

export default function ListComments(props) {
    const {postId, pageSize = 5} = props;
    const [loaded, setLoaded] = useState(false)
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [updated, setUpdated] = useState(false);
    const [serverError, setServerError] = useState(false);

    useEffect(() => {
        fetchComments();
    },[updated, page]);

    const fetchComments = async () => {
        console.log('Fetching comments for page ', page);
        const response = await CommentService.getCommentsByPostId(postId, {page: page - 1, size: pageSize});
        if(response.status >= 200 && response.status < 300){
            const data = await response.json();
            setTotalPages(data.page.totalPages)
            if(data.page.number < data.page.totalPages)
                setComments(data._embedded.comments);
            else
                setComments([]);
            setLoaded(true);
            setUpdated(true);
        }
        else{
            setServerError(true);
        }
        
    }

    const deleteComment = async (commentId) => {
        console.log('Deleting comment with id', commentId);
        const response = await CommentService.deleteComment(postId, commentId);
        if(response.status >= 200 && response.status < 300){
            setUpdated(false);
        }
    }

    const renderComponent = () => {
        return (<div className='container my-3'>
        <div className='text-center mt-4 fs-4 text-muted'><p>Comment section</p></div>
            <div className='d-flex justify-content-center row'>
                <div className='col-8 my-3'>
                    <EditComment postId={postId} onUpdate={() => setUpdated(false)}/>
                </div>
                <div className='col-8'>
                    {
                    comments.length > 0 ?
                    comments.map((comment, i) => {
                        const d = new Date(comment.publishedAt);
                        const datestring = d.getHours() + ":" + d.getMinutes() + " " + d.getDate()  + "/" + (d.getMonth()+1) + "/" + d.getFullYear();
                        return (
                            <div  key={i} className='card mb-3'>
                                <div className='card-body'>
                                    <div className='text-muted small'>
                                    <span className='fw-bolder'>Published at:</span> {datestring}
                                    </div>
                                    <div className='text-muted small'><span className='fw-bolder'>From:</span> {comment.author}</div>
                                    <p className='card-text mt-1'>
                                        {comment.content}
                                    </p>
                                    <div className='text-muted small text-end mt-1'>
                                        <Link to={'#'} className='link-dark' onClick={() => deleteComment(comment.id)}>Delete</Link>
                                    </div>
                            </div>
                        </div>)
                    })
                    :
                    <div className='text-muted text-center'>No comments yet</div>
                    }
                </div>
            </div>
            
        {totalPages > 1 &&
            <PageRow totalPages={totalPages} currentPage={page} onPageChange={page => { setPage(page);}} />
        }
        
    </div>
    );
    }

    return serverError? <ServerError message={'Server error occured. Unable to load comments.'}/> : loaded ? renderComponent(): <Loader />;
        
}
