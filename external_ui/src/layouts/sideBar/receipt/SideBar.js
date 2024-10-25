import styles from '../SideBar.module.css'
import {Link} from 'react-router-dom';
import RoutesConstant from "../../../RoutesConstant";

function SideBar(){

    return(
        <ul className={styles.SideBar}>
            <li><Link to={RoutesConstant.RECEIPT.RECEIPT_PATIENT}>Tiếp đón bệnh nhân</Link></li>
            <li><Link to={RoutesConstant.RECEIPT.PAYMENT}>Thanh toán</Link></li>
        </ul>
    )
}

export default SideBar