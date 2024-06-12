var app = new Vue({
    el: "#proxy-router-app",
    data: {
        selectMenu: 'INSTANCE',

        platformInstances: [],
        platformSearch: '',

        pluginFiles: [],
        pluginSearch: ''
    },
    methods: {
        selectAndRefreshPlatformInstances() {
            app.selectMenu = 'INSTANCE';
            app.refreshPlatformInstances();
        },
        refreshPlatformInstances() {
            this.$http.get(urlPrefix + '/platform-management/instance/list').then((response) => {
                this.$notify.success({
                    title: '数据拉取成功',
                    message: '平台实例已刷新',
                    position: 'bottom-right'
                });
                app.platformInstances = response.body;
                this.$forceUpdate();
            }, (response) => {
                this.$notify.error({
                    title: '数据拉取失败',
                    message: '平台实例刷新失败',
                    position: 'bottom-right'
                });
                app.platformInstances = [];
                console.log(response);
            });
        },
        loadInstance(row) {
            let url = urlPrefix + '/platform-management/instance/' +
                (row.loaded ? 'load' : 'unload') + '?platformId=' + row.platformId;
            this.$http.get(url).then((response) => {
                if (row.loaded) {
                    this.$notify.success({
                        title: '数据提交成功',
                        message: '平台实例已加载',
                        position: 'bottom-right'
                    });
                } else {
                    this.$notify.success({
                        title: '数据提交成功',
                        message: '平台实例已卸载',
                        position: 'bottom-right'
                    });
                }
                app.refreshPlatformInstances();
            }, (response) => {
                if (row.loaded) {
                    this.$notify.error({
                        title: '数据提交失败',
                        message: '平台实例加载失败',
                        position: 'bottom-right'
                    });
                } else {
                    this.$notify.error({
                        title: '数据提交失败',
                        message: '平台实例卸载失败',
                        position: 'bottom-right'
                    });
                }
                console.log(response);
            });
        },
        selectAndRefreshPluginFiles() {
            app.selectMenu = 'PLUGIN';
            app.refreshPluginFiles();
        },
        refreshPluginFiles() {
            this.$http.get(urlPrefix + '/platform-management/plugin/list').then((response) => {
                this.$notify.success({
                    title: '数据拉取成功',
                    message: '插件文件已刷新',
                    position: 'bottom-right'
                });
                app.pluginFiles = response.body;
            }, (response) => {
                this.$notify.error({
                    title: '数据拉取失败',
                    message: '插件文件刷新失败',
                    position: 'bottom-right'
                });
                app.pluginFiles = [];
                console.log(response);
            });
        },
        formatDate(row) {
            let date = new Date(parseInt(row.lastModified));
            return date.toLocaleString();
        },
        loadPlugin(row) {
            let url = urlPrefix + '/platform-management/plugin/' +
                (row.loaded ? 'load' : 'unload') + '?path=' + row.path;
            this.$http.get(url).then((response) => {
                if (row.loaded) {
                    this.$notify.success({
                        title: '数据提交成功',
                        message: '插件文件已加载',
                        position: 'bottom-right'
                    });
                } else {
                    this.$notify.success({
                        title: '数据提交成功',
                        message: '插件文件已卸载',
                        position: 'bottom-right'
                    });
                }
            }, (response) => {
                if (row.loaded) {
                    this.$notify.error({
                        title: '数据提交失败',
                        message: '插件文件加载失败',
                        position: 'bottom-right'
                    });
                } else {
                    this.$notify.error({
                        title: '数据提交失败',
                        message: '插件文件卸载失败',
                        position: 'bottom-right'
                    });
                }
                console.log(response);
            });
        },
        deletePluginFile(row) {
            let url = urlPrefix + '/platform-management/plugin/delete?path=' + row.path;
            this.$http.get(url).then((response) => {
                this.$notify.success({
                    title: '数据提交成功',
                    message: '插件文件已删除',
                    position: 'bottom-right'
                });
                app.refreshPluginFiles();
            }, (response) => {
                this.$notify.error({
                    title: '数据提交失败',
                    message: '插件文件删除失败',
                    position: 'bottom-right'
                });
                console.log(response);
            });
        }
    },
    computed: {},
    mounted() {
        //console.log(this.$route);
        this.refreshPlatformInstances();
    }
});