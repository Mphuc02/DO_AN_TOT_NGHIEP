
const FormatCreatedDate = (input) => {
    const date = new Date(input);

    const isSameDay = new Date().toDateString() === date.toDateString();

    if (isSameDay) {
        return date.toLocaleTimeString('en-GB', { hour12: false, hour: '2-digit', minute: '2-digit' });
    } else {
        return date.toLocaleString('en-GB', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour12: false,
            hour: '2-digit',
            minute: '2-digit'
        });
    }

}

const GetTodayString = () => {
    const timeZone = 'Asia/Ho_Chi_Minh';

    const today = new Date().toLocaleString('en-CA', {
        timeZone: timeZone,
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });

    const todayTime = today.split(',')[0]; // Kết quả đã là "YYYY-MM-DD"
    return today
}

export {GetTodayString, FormatCreatedDate}