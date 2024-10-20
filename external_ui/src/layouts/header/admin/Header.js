import {Link, Route, Routes} from "react-router-dom";

import styles from '../Header.module.css'
import {useEffect, useState} from "react";
import {JwtService} from '../../../service/JwtService'

function Header() {
    const [fullName, setFullName] = useState('');

    useEffect(() => {
         const userName = JwtService.geUserFromToken();
         if(userName != null)
             setFullName(userName)
    }, []);

    return (
            <header className={styles.wrapper}>
                <nav>
                    <li><Link to="/admin">Home</Link></li>
                </nav>
                {fullName ? (<p>Welcome, {fullName}!</p>
                ) : (<li><a href="/admin/login">Login</a></li>
                )}
            </header>
    )
}

export default Header