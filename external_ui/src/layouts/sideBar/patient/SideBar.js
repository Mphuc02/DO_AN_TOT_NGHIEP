import styles from '../SideBar.module.css'
import {Link} from 'react-router-dom';

function SideBar(){

    return(
        <ul className={styles.SideBar}>
            <li><Link to={"/patient/diagnostics"}>Chuẩn đoán bệnh</Link></li>
            <li><Link to={"/patient/appointments-management"}>Quản lý lịch hẹn</Link></li>
            <li><Link to={"/patient/examination-histories"}>Lịch sử khám bệnh</Link></li>
        </ul>
    )
}

export default SideBar