import Header from "../../header/receipt/Header";
import SideBar from "../../sideBar/receipt/SideBar";
import styles from '../style.module.css'
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import RoutesConstant from "../../../RoutesConstant";

function ReceiptLayout({children}){
    const [receipt, setReceipt] = useState(null)
    const navigate = useNavigate()

    useEffect(() => {
        const receipt = JSON.parse(localStorage.getItem('receipt'))
        if(!receipt){
            return
        }
        if(!receipt.roles.includes('RECEPTION_STAFF')){
            localStorage.removeItem('access-token')
            localStorage.removeItem('refresh-token')
            localStorage.removeItem('receipt')

            navigate(RoutesConstant.RECEIPT.LOGIN)
        }else {
            setReceipt(receipt)
        }
    }, []);

    return(
        <div className={styles.AllScreen}>
            <div className={styles.Header}>
                <Header receipt={receipt}/>
            </div>

            <div className={styles.SideBar}>
                <SideBar receipt={receipt}/>
            </div>

            <div className={styles.Children}>
                {children}
            </div>
        </div>

    )
}

export default ReceiptLayout