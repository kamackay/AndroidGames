$.fn.setupHorizontalScroll = function () {this.mousewheel(scrollHorizontal);}

var settings = {anim: true}
const colors = {
  materialPrimary: '#009688',
  white: '#ffffff',
  materialWarning: '#ff5722',
  black: '#000000', materialBlue: '#1D62AC'
}

var tooltipOptions = {}

function getStoredBool (key, def = true) {
  var data = getData(key)
  return def ? data !== 'false' : data === 'true'
}

function openInNewTab (url) { var e = window.open(url, '_blank')
  e.focus() } function isMobileDevice () { var e = !1
  return (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0, 4))) && (e = !0), e } function redirectTo (e) { window.location.href = e } function convertToGoogleSearch (e) { for (var o = 'https://www.google.com/webhp#q=', t = 0, i = e.length; i > t; t++)o += ' ' == e[t] ? '+' : '#' == e[t] ? '%23' : '/' == e[t] ? '%2F' : '%' == e[t] ? '%25' : '@' == e[t] ? '%40' : '`' == e[t] ? '%60' : '=' == e[t] ? '%3d' : '+' == e[t] ? '%2b' : '\\' == e[t] ? '%5c' : e[t]; return o } function storeData (e, o) { 'undefined' != typeof Storage && localStorage.setItem(e, o) } function getData (e) { return 'undefined' != typeof Storage ? localStorage.getItem(e) : null } function writeToLog (e) { try { var o = $('#keithapps-log')
    void 0 !== o && o.append('<p>' + e + '</p>') } catch (t) {} } function removeContextMenu (e = '*') { var o = $('body').find(e)
  $.each(o, function (e, o) { $(this).on('contextmenu', function (e) { e.stopPropagation()
      e.preventDefault()
      e.stopImmediatePropagation(); }) }); $('body').on('contextmenu', function (e) { e.stopPropagation()
    e.preventDefault()
    e.stopImmediatePropagation(); }) } function isIE () { return getIEVersion() !== -1 } function getIEVersion () { var rv = -1
  if (navigator.appName == 'Microsoft Internet Explorer') { var ua = navigator.userAgent
    var re = new RegExp('MSIE ([0-9]{1,}[\.0-9]{0,})')
    if (re.exec(ua) != null) rv = parseFloat(RegExp.$1); } return rv; }
function getDayOfWeek (time = null) {
  time = time || new Date()
  var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
  return days[new Date().getDay()]
}
function getMonthName (time = null) {
  time = time || new Date()
  var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
  return months[time.getMonth()]
}
function getDayOfMonth (time = null) {
  time = time || new Date()
  var d = time.getDate()
  var s = '' + d
  switch (d) {
    case 1:
      return s + 'st'
    case 2:
      return s + 'nd'
    case 3:
      return s + 'rd'
    default:
      return s + 'th'
  }
}

function daysInMonth (date = new Date()) {
  var d = new Date(date.getFullYear(), date.getMonth() + 1, 0)
  return d.getDate()
}
/**/
$(document).ready(function () {
  $.each($('body').find('*'), function (a, b) {
    var attr = $(this).attr('data-textsize')
    if (typeof attr !== typeof undefined && attr !== false) {
      var obj = $(this)
      var f = function () {
        if ($(window).height() > $(window).width())
          obj.css('font-size', '' + (attr / 2) + 'px')
        else obj.css('font-size', '' + attr + 'px')
      }
      $(window).resize(f)
      f()
    }
  })
}) /**/

function showSnackbar (text) {
  try {
    $.snackbar({
      content: text
    })
  } catch (err) {
    // alert(err)
  }
}

function notSupported () {
  try {
    showSnackbar('Sorry, but this is not currently supported. Check back in a few days!')
  } catch (err) {}
}

function showWatermark (anim = true) {
  $('body').append('<div onclick="openInNewTab(\'http://keithmackay.com\')" id="watermark" style="position: fixed; top: -100%;left: 200%;width: 200px; opacity: .5; cursor: pointer;"><img style="width: 200px;" src="http://keithmackay.com/images/watermark.png" alt="watermark"/></div>')
  var place = function (time = 10) {$('#watermark').animate({left: $(window).width() - 200, top: 0}, {duration: time, queue: false, easing: 'swing'});if ($(window).height() > $(window).width())$('#watermark').hide()}
  if (anim) place(10000)
  else $('#watermark').css({'left': $(window).width() - 200, 'top': 0})
  $(window).resize(function () {place();})
}

function rand (min, max) {
  return Math.random() * max + min
} function randInt (min, max) {
  return Math.floor(rand(min, max))
}

var opt = {
  typingSpeedMin: 20,
  typingSpeedMax: 100,
  formatNewLines: true,
}
$.fn.flash = function (interval) {
  var vis = true,
    t = $(this)
  window.setInterval(function () {
    if (vis) t.fadeOut(100)
    else t.fadeIn(100)
    vis = !vis
  }, interval)
}

$.fn.typeOut = function (str, complete = function () {} , clear = true) {
  if (str.length === 0) {
    complete()
    return
  }
  var t = $(this)
  if (clear) t.html('')
  var c = str.charAt(0)
  var s
  switch (c) {
    case '\n':
      s = '<br>'
      break
    case '\t':
      s = '<p style="display:inline-block;">&nbsp;&nbsp;&nbsp;&nbsp;</p>'
      break
    case ' ':
      c = '&nbsp;'
    default:
      s = '<p style="display:inline-block;">' + c + '</p>'
      break
  }
  t.append(s)
  setTimeout(function () {
    t.typeOut(str.substr(1), complete, false)
  }, rand(opt.typingSpeedMin, opt.typingSpeedMax))
}

function collapseSettings (opt = '') {
  var settingsEl = $('#settings')
  if (settingsEl.length) {
    var closeButton = settingsEl.find('.closeSettings')
    if (closeButton.length) {
    } else {
      settingsEl.append('<div style="display:inline;cursor:pointer;height:100%;margin:10px;" onclick="collapseSettings();storeData(\'settingsCollapse\', 1)" class="closeSettings"><i style="color:red;padding:10px;" class="material-icons">close</i></div>')
    }
    settingsEl.animate({left: (-2 * settingsEl.width())}, {duration: (opt === 'fast') ? 1 : 500, queue: false})
    var shortcut = $('#settingsShortcut')
    if (shortcut.length) {
      setTimeout(function () {shortcut.show()}, 550)
    } else {
      $('body').append('<div style="position:fixed;left:2px;bottom:2px;cursor:pointer;" id="settingsShortcut" onclick="storeData(\'settingsCollapse\', 0);setTimeout(function (){expandSettings(false)}, 100);"><i style="font-size:35px;color:inherit;" class="material-icons">settings</i></div>')
      shortcut = $('#settingsShortcut')
    }
    shortcut.hover(function () {
      shortcut.animate({color: colors.materialWarning}, {duration: 500, queue: false})
    }, function () {
      shortcut.animate({color: colors.black}, {duration: 500, queue: false})
    })
  }
}

function expandSettings (force = true) {
  // if (getData('settingsCollapse') && force) {console.log('optionsSayNo'); return;}
  var settingsEl = $('#settings')
  if (settingsEl.length) {
    settingsEl.css('background-color', '#eee').animate({left: 0}, {duration: 1500, queue: false})
    var shortcut = $('#settingsShortcut')
    if (shortcut.length) shortcut.hide()
  }
}

var scrollHorizontal = function (event, delta) {
  this.scrollLeft -= (delta * 30)
  event.preventDefault()
}

$(document).ready(function () {
  $(function () {
    $('[data-toggle="tooltip"]').tooltip(tooltipOptions)
  })
  var leftBar = $('#leftBar')
  var a1 = function () {
    if (leftBar.length) $.each(leftBar.find('.option'), function (n, o) {
        var t = $(this)
        var w = Math.max(leftBar.width() / 2, 30)
        t.css({'height': w, 'font-size': w / 3, 'padding-top': w / 6,
        'width': w, 'margin-left': leftBar.width() / 4})
        t.hover(function () {
          t.animate({backgroundColor: colors.materialPrimary}, {duration: 500,queue: false}).animate({color: colors.white}, {duration: 500,queue: false})
        }, function () {
          t.animate({backgroundColor: colors.white}, {duration: 500,queue: false}).animate({color: colors.black}, {duration: 500,queue: false})
        })
      })}
  a1()
  $(window).resize(a1)
  $('head').append("<script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');ga('create', 'UA-80824635-1', 'auto');ga('send', 'pageview');</script>")
})
function toast (text) {showSnackbar(text);}

function usingMaterialLoading () {
  var materialFunction = function () {
    var materialLoaders = $('body').find('.materialLoad:not(.materialLoad-Handled)')
    if (materialLoaders.length)$.each(materialLoaders, function (n, o) {
        var t = $(this)
        t.append('<svg class="spinner" width="65px" height="65px" viewBox="0 0 66 66" xmlns="http://www.w3.org/2000/svg"><circle class="path" fill="none" stroke-width="6" stroke-linecap="round" cx="33" cy="33" r="30"></circle></svg>')
        t.addClass('materialLoad-Handled')
      })
  }
  setInterval(materialFunction, 50)
}

var A_BLOCKS = [
  '(' +
  /"[^"\\]*(?:\\[\S\s][^"\\]*)*"|'[^'\\]*(?:\\[\S\s][^'\\]*)*'/.source,
  /(?:[$\w\)\]]|\+\+|–)\s*(\/)(?![*\/])/.source,
  /\/(?=[^*\/])[^[\/\\]*(?:(?:\[(?:\\.|[^\]\\]*)*\]|\\.)[^[\/\\]*)*?(\/)[gim]*/.source +
  ')',
  /\/(\*)[^*]*\*+(?:[^*\/][^*]*\*+)*\//.source,
  /\/\/[^\r\n]*/.source
]
var R_BLOCKS = new RegExp(A_BLOCKS.join('|'), 'gm')

// remove comments, keep other blocks
function stripComments (str) {
  return str.replace(R_BLOCKS, function (match, qblock, mlcomm) {
    return qblock ? match : // quoted string, divisor, regex: return as-is
      mlcomm ? ' ' : // multiline comment: replace with space
        '' // single comment: remove
  })
}

// Convert all comments to block comments, for the sake of minification
function commentsToBlock (str) {
  return str.replace(R_BLOCKS, function (match, qblock, mlcomm) {
    return qblock ? match : // quoted string, divisor, regex: return as-is
      mlcomm ? match : // multiline comment: keep
        match.replace(/\/\//g, '/*') + '*/' // single comment: convert to multiline
  })
}

// Test with “node rmcomms file_to_test.js"
function js_minify (txt, keepComments = false) {
  txt = keepComments ? commentsToBlock(txt) : stripComments(txt)
  var minCode = '', deleteNextWhitespace = false
  for (var i = 0; i < txt.length; i++) {
    var c = txt[i]
    switch (c) {
      case ' ':
        if (!deleteNextWhitespace)minCode += c
        break
      case '{':
        deleteNextWhitespace = true
        minCode += c
        break
      default:
        if (deleteNextWhitespace) deleteNextWhitespace = false
        minCode += c
        break
      case '\n':
      case '\t':
        // Do Nothing
        break
    }
  }
  return minCode
}

function loadJSAsync (url, id) {
  try {
    $.get(url, function (data) {
      $('head').append('<script id="' + id + '" type="text/javascript">' + data + '</script>')
    }).fail(function () {alert('loading ' + url + ' failed')})
  } catch (err) {
    console.log('Error loading script:: - ' + err)
  }
}
function download (filename, text) {
  var element = document.createElement('a')
  element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text))
  element.setAttribute('download', filename)

  element.style.display = 'none'
  document.body.appendChild(element)

  element.click()

  document.body.removeChild(element)
}

function loadBackgroundAsync (url) {
  $.get(url, function (data) {
    $('body').css('background-image', 'url(' + url + ')')
  // Loading the data into the src doesn't work no matter what I do, so I'm using this to hope that the get will cache the data to make this process faster
  })
}

function popupForm (contents) {
  var old = $('#popupFormDiv')
  if (old.length) old.remove()
  var $win = $(window)
  $('body').append('<div class="well" style="position:fixed;top:' + (-2 * $win.height()).toString() + 'px;left:20vw;height:60vh;width:60vw;text-align:center;" id="popupFormDiv"><i onclick="$(\'#popupFormDiv\').remove()" style="width:100%;text-align:right;" class="material-icons">clear</i></div>')
  var popupElement = $('#popupFormDiv')
  popupElement.append(contents)
  popupElement.animate({
    top: $win.height() * .2
  }, {
    duration: 2000,
    queue: false
  })
}
