//百度地图
var bmap = new BMapGL.Map('container'); // 创建Map实例
bmap.centerAndZoom(new BMapGL.Point(130.305583,47.346484), 14); // 初始化地图,设置中心点坐标和地图级别
bmap.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放
bmap.setMapType(BMAP_EARTH_MAP);
