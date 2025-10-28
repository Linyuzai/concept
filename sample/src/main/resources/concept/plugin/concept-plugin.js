/*var encodePassword = function (password) {
    return btoa(password);
};

var decodePassword = function (password) {
    return atob(password);
}*/

function encodePassword(password) {
    console.log('start encodePassword');
    return btoa(password);
}

function decodePassword(password) {
    console.log('start encodePassword');
    return atob(password);
}

function init() {
    console.log('start init');
    axios.interceptors.request.use(config => {
        config.headers['Authorization'] = localStorage.getItem("token");
        return config;
    });
    axios.get('/plugin2/token').then(response => {

    }).catch(e => {
        console.log(e);
    });
}
