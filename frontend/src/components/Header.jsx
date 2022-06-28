import React, { Component } from 'react'
import { Link } from 'react-router-dom'

export default class Header extends Component {
  render() {
    return (
            <div className='container'>
            <nav className="navbar navbar-expand-lg navbar-light mt-5" >
                <div className='navbar-text navbar-brand'><Link className='nav-link' to="/" >SaR Cats</Link></div>
                    <ul className="navbar-nav">
                        <li className='nav-item'>
                            <Link className='nav-link' to="/" >Home</Link>
                        </li>
                        <li className='nav-item'>
                            <Link className='nav-link' to="/posts" >View Posts</Link>
                        </li>
                        <li className='nav-item'>
                            <Link className='nav-link' to="/posts/create" >Create Post</Link>
                        </li>
                        <li className='nav-item'>
                            <Link className='nav-link' to="about" >About</Link>
                        </li>
                    </ul>
                </nav>
                 <hr/>
            </div>
                
    )
  }
}
