import styles from '../SideBar.module.css'
import {Link} from 'react-router-dom';

function SideBar(){
    const currentPath = window.location.pathname

    return(
        <ul className={styles.SideBar}>
            <li><Link to={"/admin/employee-management"}>Quản lý thông tin nhân viên</Link></li>
            <li>
                <Link to={"/admin/medicine-management"}>Quản lý thông tin thuốc</Link>

                {currentPath.startsWith('/admin/medicine-management') &&
                    <ul>
                        <li><Link to={'/admin/medicine-management/origin'}>Quản lý nguồn gôc thuốc</Link> </li>
                    </ul>}
            </li>
            <li><Link to={'/admin/examination-room-management'}>Quản lý thông tin phòng khám</Link></li>
            <li><a href="/admin/">Profile</a></li>
        </ul>
    )
}

export default SideBar