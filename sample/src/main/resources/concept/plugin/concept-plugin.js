/*var encodePassword = function (password) {
    return btoa(password);
};

var decodePassword = function (password) {
    return atob(password);
}*/

function encodePassword(password) {
    return btoa(password);
}

function decodePassword(password) {
    return atob(password);
}

function init() {
    axios.interceptors.request.use(config => {
        config.headers['Authorization'] = localStorage.getItem("token");
        return config;
    });
    axios.get('/plugin2/token').then(response => {

    }).catch(e => {
        console.log(e);
    });
}
