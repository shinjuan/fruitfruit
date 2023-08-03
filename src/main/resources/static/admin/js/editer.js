$(document).on('change', '#description', (e) =>{
    fireBaseUpload('#description', 'description/img').then((res)=>{
        const imgUrl = `<img src="${res.fileUrl}" data-filename="${res.fileName}" data-filepath="${res.path}">`
        // tinymce.execCommand('mceInsertContent', false, imgUrl);
        tinymce.execCommand('mceInsertContent', false, imgUrl);

    })

})


fireBaseUpload = ((fileEl, path) => {
    if(fileEl === undefined || path === undefined){
        console.error('param is undefined !!');
        console.table({fileEl: '파일 컴포넌트', path: '파이어베이스 폴더명'});
        return
    }

    return new Promise((resolve, reject) =>{

        const getFiles = $(`${fileEl}`)[0].files

        $(getFiles).each((idx, item) =>{
            axios({
                headers: {'Content-Type': 'multipart/form-data'},
                method: 'post',
                url : 'uploadFiles',
                data : {
                    file: item,
                    path: path,
                    fileName: item.name
                },
            }).then((res) =>{
                const resObj = {
                    fileName: item.name,
                    path: path,
                    fileUrl: res.data
                }
                resolve(resObj)

                //파일 컴포넌트 초기화
                $(`${fileEl}`).val('')

            }).catch((error)=>{
                reject(error);
            })
        })
    })
})