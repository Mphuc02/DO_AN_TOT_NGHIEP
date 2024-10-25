import {Link} from "react-router-dom";
import styles from '../Header.module.css'
import {useEffect, useState} from "react";
import {JwtService} from '../../../service/JwtService'
import RoutesConstant from "../../../RoutesConstant";

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
                <li><Link to={RoutesConstant.RECEIPT.RECEIPT_PATIENT}>Tiếp đón bệnh nhân</Link></li>
            </nav>
            {fullName ? (<p>Welcome, {fullName}!</p>
            ) : (<li><a href={RoutesConstant.RECEIPT.LOGIN}>Đăng nhập</a></li>
            )}
        </header>
    )
}

export default Header