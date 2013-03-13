(function(jQuery) {
    // Code from jQuery.
    // Because jQuery.browser is deprecated now.
    function uaMatch() {
        var ua = window.navigator.userAgent.toLowerCase(),
            rwebkit = /(webkit)[ \/]([\w.]+)/,
            ropera = /(opera)(?:.*version)?[ \/]([\w.]+)/,
            rmsie = /(msie) ([\w.]+)/,
            rmozilla = /(mozilla)(?:.*? rv:([\w.]+))?/,
            match = rwebkit.exec(ua) ||
                ropera.exec(ua) ||
                rmsie.exec(ua) ||
                ua.indexOf("compatible") < 0 && rmozilla.exec(ua) || [],
            browser = match[1] || false;

        if (browser) {
            switch (browser) {
                case 'webkit':
                    return '-webkit-';
                case 'opera':
                    return '-o-';
                case 'msie':
                    return '-ms-';
                    break;
                case 'mozilla':
                    return '-moz-';
                default:
                    return '';
            }
        }
        return '';
    }
    var prefix = uaMatch();

    jQuery.fn.clock = function (options) {
        var settings = $.extend( {
            size:        150,
            graduations: 4,
            speed: 1000,
            date: null
        }, options);

        return this.each(function () {
            var $this = jQuery(this), date = null, i,
                transform = prefix + 'transform',
                $clock = jQuery('<div class="jqc-clock-face"></div>')
                    .css({
                        position: 'absolute',
                        borderRadius: settings.size/2,
                        height: settings.size,
                        width: settings.size
                    }),
                $sec = jQuery('<div class="jqc-clock-rotat jqc-clock-sec"><span></span></div>'),
                $min = jQuery('<div class="jqc-clock-rotat jqc-clock-min"><span></span></div>'),
                $hour = jQuery('<div class="jqc-clock-rotat jqc-clock-hour"><span></span></div>');

            try {
                settings.date.getSeconds();
                date = settings.date;
            } catch (e) {
                date = new Date();
            }

            if ($this.css('position') != 'absolute') {
                $this.css('position', 'relative');
            }

            if (settings.graduations) {
                if (settings.graduations == 4) {
                    for (i = 0; i < 4; i++) {
                        $clock.append('<div class="jqc-clock-rotat jqc-drad jqc-drad'+ (i * 3) +'"><span></span></div>');
                    }
                } else {
                    for (i = 0; i < 12; i++) {
                        $clock.append('<div class="jqc-clock-rotat jqc-drad jqc-drad'+ i +'"><span></span></div>');
                    }
                }
            }

            $clock.append($sec);
            $clock.append($min);
            $clock.append($hour);
            $this.append($clock);

            function update() {
                $sec.css(transform, 'rotate(' + (360 / 60 * date.getSeconds()) + 'deg)')
                $min.css(transform, 'rotate(' + (360 / 60 * date.getMinutes()) + 'deg)')
                $hour.css(transform, 'rotate(' + ((360 / 12 * date.getHours()) + (30 / 60 * date.getMinutes())) + 'deg)')
            }

            update();
            window.setInterval(function () {
                date.setSeconds(date.getSeconds() + 1);
                update();
            }, settings.speed);
        });
    };
}(jQuery));
