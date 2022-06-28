import React from 'react'

export default function Errors(props) {
    const {errors } = props;
  return (
    <div className='contatiner'>
        {
            errors.map((e, i) => 
                (<div className='text-danger' key={i} >
                    {e}
                </div>)
            )}
    </div>
  )
}
