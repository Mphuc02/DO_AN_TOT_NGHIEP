import Header from "../../header/receipt/Header";
import SideBar from "../../sideBar/receipt/SideBar";
import styles from '../style.module.css'

function ReceiptLayout({children}){
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

export default ReceiptLayout