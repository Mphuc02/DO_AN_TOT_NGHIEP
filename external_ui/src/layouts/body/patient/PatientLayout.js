import Header from "../../header/patient/Header";
import SideBar from "../../sideBar/patient/SideBar";
import styles from '../style.module.css'

function PatientLayout({children}){
    return(
        <div className={styles.AllScreen}>
            <div className={styles.Header}>
                <Header/>
            </div>

            <div className={styles.SideBar}>
                <SideBar/>
            </div>

            <div className={styles.Children}>
                {children}
            </div>
        </div>

    )
}

export default PatientLayout