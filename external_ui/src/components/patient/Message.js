import {useEffect, useRef, useState} from "react";
import styles from '../../layouts/body/style.module.css'
import {SendApiService} from "../../service/SendApiService";
import {Chat, EMPLOYYEE, WEBSOCKET} from "../../ApiConstant";
import WebSocketService from "../../service/WebSocketService";
import {JwtService} from "../../service/JwtService";
import {ConvertBlobUrlToBase64} from "../../service/BlobService";

const ChatWindow = ({relationShip, webSocket}) => {
    const [pageAble, setPableAble] = useState(0)
    const [messages, setMessages] = useState([])
    const [content, setContent] = useState('')
    const [previewImage, setPreviewImage] = useState(null)
    const fileInputRef = useRef(null)
    const [decodedImage, setDecodedImage] = useState(null)

    const getAllMessage = () => {
        SendApiService.getRequest(Chat.Message.getMessageByReceiverId(relationShip.doctorId, pageAble), {}, response => {
            setMessages(prev => {
                const newArr = [...prev]
                newArr.push(...response.data.content)
                return newArr
            })
        }, error => {

        })
    }

    useEffect(() => {
        getAllMessage()
    }, [relationShip.id]);

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file && file.type.startsWith('image/')) {
            const preview = URL.createObjectURL(file)
            setPreviewImage(preview)
            setDecodedImage(ConvertBlobUrlToBase64(preview))
        }
    };

    const onclickSendButton = () => {
        if(!content && !decodedImage){
            return
        }

        const test = Promise.resolve(decodedImage)
        test.then(result => {
            const newMessage = {
                receiverId: relationShip.doctorId,
                content: content
            }

            if(result){
                SendApiService.postRequest(Chat.Message.sendImage(relationShip.doctorId), result, {} , response => {

                }, error => {

                })
            }else{
                SendApiService.postRequest(Chat.Message.getUrl(), newMessage, {} , response => {

                }, error => {

                })
            }

        })

        setContent('')
        setDecodedImage('')
        setPreviewImage('')
        fileInputRef.current.value = ''
    }

    const deleteSelectedImage = () => {
        setDecodedImage('')
        setPreviewImage(prev => {
            URL.revokeObjectURL(prev)
            return null
        })
        fileInputRef.current.value = ''
    }

    return (<div>
        {messages.map((message, index) => {
            return <div key={message.id}>
                {message.content}
            </div>
        })}

        <textarea value={content} onChange={(e) => setContent(e.target.value)}/>
        <button onClick={() => onclickSendButton()}>Gửi</button>

        {previewImage &&
            <div>
                <img className={styles.previewChatImage} src={previewImage}/>
                <button onClick={() => deleteSelectedImage()}>Xoá ảnh này</button>
            </div>
        }
        <div>
            <label>Chọn ảnh</label>
            <input
                type="file"
                ref={fileInputRef}
                accept="image/*"
                onChange={handleImageChange} />
        </div>
    </div>)
}

const Message = () => {
    const [relationShipMap, setRelationShipMap] = useState(new Map())
    const [selectedRelationShip, setSelectedRelationShip] = useState('')
    const [page, setPage] = useState(0)
    const [webSocket, setWebSocket] = useState(new WebSocketService())

    const getDoctorInformation = (doctorIdsMap) => {
        const ids = []
        for(const [key, value] of doctorIdsMap){
            ids.push(key)
        }
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(EMPLOYYEE.findByIds(), ids, {}, response => {
                for(const doctor of response.data){
                    const relationShip = doctorIdsMap.get(doctor.id)
                    relationShip.doctor = doctor
                    resolve()
                }
            }, error => {
                reject(error)
            })
        })
    }

    const findRelationShipOfPatient = () => {
        SendApiService.getRequest(Chat.RelationShip.findRelationShipsOfPatient(page), {}, async response => {
            const tempMap = new Map()
            const doctorIdsMap = new Map()
            const content = response.data.content
            if(!content){
                return
            }
            for(const relationShip of content){
                tempMap.set(relationShip.id, relationShip)
                doctorIdsMap.set(relationShip.doctorId, relationShip)
            }
            await getDoctorInformation(doctorIdsMap)
            setRelationShipMap(tempMap)

        }, error => {

        })
    }

    const connectToWebSocket = () => {
        const topics = [WEBSOCKET.chat(JwtService.geUserFromToken())]
        webSocket.connectAndSubscribe(topics, (message) => {
            console.log(message)
        })
    }

    const disconnect = () => {
        webSocket.disconnect()
    }

    useEffect(() => {

    }, []);

    useEffect(() => {
        findRelationShipOfPatient()
        connectToWebSocket()

        return () => {
            disconnect()
        }
    }, []);

    return (<>
        <h2>Tin nhắn tư vấn</h2>
        <div className={`${styles.height100} ${styles.divFlex}`}>
            <div className={`${styles.width20} ${styles.height100} ${styles.scrollY}` }>
                {[...relationShipMap].map(([key, value]) => {
                    const doctor = value.doctor
                    if(!doctor){
                        return null
                    }
                    const fullName = doctor.fullName
                    return <div className={styles.cursorPointer} key={key} onClick={() => setSelectedRelationShip(value)}>
                                {fullName.firstName + " " + fullName.middleName + " " + fullName.lastName}
                            </div>
                })}
            </div>
            <div className={`${styles.width80} ${styles.scrollY}`}>
                <ChatWindow relationShip={selectedRelationShip} webSocket={webSocket}/>
            </div>
        </div>
    </>)
}

export {Message}