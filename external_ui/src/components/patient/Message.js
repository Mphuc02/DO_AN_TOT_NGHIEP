import {useEffect, useState} from "react";
import styles from '../../layouts/body/style.module.css'
import {SendApiService} from "../../service/SendApiService";
import {Chat} from "../../ApiConstant";
const Message = () => {
    const [relationShipMap, setRelationShipMap] = useState(new Map())
    const [selectedRelationShip, setSelectedRelationShip] = useState('')
    const [page, setPage] = useState(0)

    const getDoctorInformation = (, ids) => {

    }

    const findRelationShipOfPatient = () => {
        SendApiService.getRequest(Chat.RelationShip.findRelationShipsOfPatient(page), {}, response => {
            const tempMap = new Map()
            const doctorIdSet = new Set()
            const content = response.data.content
            if(!content){
                return
            }
            for(const relationShip of content){
                tempMap.set(relationShip.id, relationShip)
                doctorIdSet.add(relationShip.doctorId)
            }
            setRelationShipMap(tempMap)

        }, error => {

        })
    }

    useEffect(() => {
        findRelationShipOfPatient()
    }, []);

    return (<>
        <h2>Tin nhắn tư vấn</h2>
        <div className={`${styles.height100} ${styles.divFlex}`}>
            <div className={`${styles.width20} ${styles.height100} ${styles.scrollY}` }>
                {[...relationShipMap].map(([key, value]) => {
                    return key
                })}
            </div>
            <div className={`${styles.width80} ${styles.scrollY}`}></div>
        </div>
    </>)
}

export {Message}