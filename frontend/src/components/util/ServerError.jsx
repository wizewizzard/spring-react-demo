import React from 'react'

export default function ServerError(props) {
  const {message='Sorry server error occured'} = props;
  return (
    <div className='container'>
        <div>
            {message}
        </div>
    </div>
  )
}
