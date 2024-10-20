import styles from '../SideBar.module.css'
import {Link} from 'react-router-dom';

function SideBar(){

    return(
        <ul className={styles.SideBar}>
            <li><Link to={"/employee/doctor/working-schedule-management"}>Quản lý lịch làm việc</Link></li>
            <li><Link to={"/employee/doctor/examination-result-histories"}>Xem lịch sử khám việc</Link></li>
        </ul>
    )
}

export default SideBar