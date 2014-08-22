var BadgePlugin = function () {};

BadgePlugin.prototype = {
    /**
     * Sets the badge of the app icon.
     *
     * @param {Number} badge
     *      The new badge number
     */
    set: function (badge) {
        cordova.exec(null, null, 'BadgePlugin', 'setBadge', [parseInt(badge) || 0]);
    }

};

var plugin  = new BadgePlugin();

module.exports = plugin;