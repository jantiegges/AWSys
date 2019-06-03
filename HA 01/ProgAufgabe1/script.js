var g_sUsername = "anonymous"; // set default username
var g_curChannel = -1; // not set
var g_sLastSeen = "";
// global const settings
const g_iDebugMsg = 1; // 0 - off, 1 - unstable, 2 - all
const g_iRefreshChannels = 10000*6;
const g_iRefreshMsgs = 1000*10;
const g_sToken = "qdgOjossrOiE";
const g_sServer = "http://34.243.3.31:8080";
// store user and channel list since we will be accessing it frequently in different funcs
const g_sUserList = "#users .user-list .user-name p";
const g_sChannelList = "#channels .channel-name p";

$( document ).ready(function() {

    _getChannels(); // load existing channels
    //$("#chat-screen .write-msg textarea").focus(); // set focus to message input

    $("#channels i").click(function () { // user added a channel
        // fetch input objects
        let oCName = $("#channels .channel-name #in_cname");
        let oCTopic = $("#channels .channel-name #in_ctopic");
        // read data from input
        let sNewChannel = oCName.val();
        let sNewTopic = oCTopic.val();
        if (g_iDebugMsg >= 2) console.log(String.format("Trying to add New Channel: '{0}' with Topic: '{1}'", sNewChannel, sNewTopic));

        // check if channel can be added (not exists and not empty)
        if (sNewChannel !== '' && !_containsElem(g_sChannelList, sNewChannel)) {
            // TODO: <click on add channel> first we need to create the channel on the server in order to get its ID
            // _addChannelToScreen(iID??????, sNewChannel, sNewTopic); // add channel
        } else console.error(String.format("Channel '{0}' already exists!", sNewChannel));
        // clear input fields
        oCName.val('');
        oCTopic.val('');
    });

    $("#users .user-list i").click(function () { // user changed his username
        let o = $("#users .user-list textarea");
        let tmp = o.val(); // read new username
        if (tmp === '') return;
        g_sUsername = tmp; // username ok -> update
        if (!_containsElem(g_sUserList, g_sUsername)) // TODO: only add to userlist when server says we are in the channel (since we wrote something)
            _addUserToScreen(g_sUsername);
        // clear and update input:
        o.val('');
        o.attr('placeholder', g_sUsername);
    });

    $("#chat-screen .write-msg i").on('click', function () {
        _inputHelper_ReadTextarea();
        return false;
    });
    $("#chat-screen .write-msg textarea").on('keypress', function (e) { // check for Enter in input filed
        if (e.which === 13){
            _inputHelper_ReadTextarea();
            return false;
        }
    });

    // check if user clicked on a channel
    $("#channels").on("click", ".channel-name p", function () { // event delegation so that we can catch newly added channels as well
        let sChannel = $(this).text().trim(); // since text() gives a string with whitespace and spaces trim those first
        _switchChannel(sChannel);
    });

    // set timers for background poll funcs
    setTimeout(_updateChannelList, g_iRefreshChannels);
    setTimeout(_updateMessageList, g_iRefreshMsgs);
    // TODO: jede min check ob user offline
});


//#region poll funcs
function _updateChannelList() {
    _getChannels(); // update channel list
    setTimeout(_updateChannelList, g_iRefreshChannels); // reschedule call
}

function _updateMessageList() {
    if (g_curChannel === -1 || g_sLastSeen === '') return false; // not in a channel yet
    _getMessages(g_curChannel, '?lastSeenTimestamp='+encodeURIComponent(g_sLastSeen));
    setTimeout(_updateMessageList, g_iRefreshMsgs); // reschedule call
}

//#endregion background poll funcs


/*
    Function:
    Description:
    Params:
    Returns:
 */


//#region server funcs
function _sendMsg(sMsg, sSender) {
    /*
        TODO: send Msg to server
        Der Nutzer soll Nachrichten verfassen und diese in dem beigetretenen Channel veröffentlichen können.
        Eine Nachricht besteht aus Nachrichtentext („content“), Autor („creator“), einem Zeitstempel und ist einem Channel zugeordnet.
        Autor und Channel sind dabei vorbestimmt durch den Channel, dem der Nutzer im Moment mit einem Nutzernamen beigetreten ist.
        Der Zeitstempel einer Nachricht wird Serverseitig festgelegt, sobald die Nachricht an den Server gesendet wurde.
        Erfolgreich an den Server gesendete Nachrichten sollen direkt nach dem Senden an die bereits vorliegenden Nachrichten
        im Nachrichtenfenster angehängt werden.
     */

    // add us to online list if not in there yet
    if (!_containsElem(g_sUserList, sSender)) _addUserToScreen(sSender);
}

function _createNewChannel(sChannel, sTopic) {
    /*
        TODO: create channel (on server)
        Nutzer sollen Channels mit entsprechendem Namen erstellen können.
        Channels sind Chatgruppen. Jeder Channel hat ein Thema („topic“), das den Channel neben dem Namen charakterisiert.
        Serverseitig entspricht dies dem Anlegen einer neuen Channel-Ressource.
     */
    // return iID!! OR: _addChannelToScreen mit ID bei async func
}

function _getChannels(sPage='/channels?page=0&size=500') {
    // any page size works but lower values = visible loading of additional channels
    $.ajax({
        dataType: "json",
        url: g_sServer+sPage,
        type:"GET",
        headers: {"X-Group-Token": g_sToken},
        success: function (raw) {
            // get channel names + ids
            if (raw['_embedded'] === undefined) return;
            let data = raw['_embedded']['channelList'];
            $.each(data, function (key, val) {
                // add channel from given server list to local channel list
                if (!_containsElem(g_sChannelList, _prepChannelName(val.name))){ // do not add channels that are already in our list
                    _addChannelToScreen(val.id, val.name, val.topic);
                    if (val.id === 1) _switchChannel(val.name); // select first channel as default
                }
            });
            // check for additional pages of channels
            data = raw['_links']['next'];
            $.each(data, function (key, val) {
                console.log(String.format("More channels found getting: '{0}'", val));
                // recursively load next page using given link
                _getChannels(val);
            })
        }
    });
}

function _getMessages(iChannelId, sOptions='') {
    let sPage = String.format("/channels/{0}/messages{1}", iChannelId, sOptions);
    if (g_iDebugMsg >= 2) console.log('Msg-Page: '+sPage);
    let bNewMsg = false; // reduce traffic by only updating user list when we received a message
    $.ajax({
        dataType: "json",
        url: g_sServer+sPage,
        type:"GET",
        headers: {"X-Group-Token": g_sToken},
        success: function (raw) {
            // get channel names + ids
            if (raw['_embedded'] === undefined) return;
            let data = raw['_embedded']['messageList'];
            $.each(data, function (index, val) {
                // add channel from given server list to local channel list
                    if (g_iDebugMsg >= 2) console.log(String.format("[{0}] - {1}: {2}", val.timestamp, val.creator, val.content));
                    if (index === 0) g_sLastSeen = val.timestamp; // our first element is the latest one -> update lastseen timestamp
                    if (sOptions === '' || index > 0){ // Options set -> first entry is the msg we already have -> skip it
                        _addMsgToScreen(val.content, val.creator, new Date(val.timestamp), true);
                        bNewMsg = true
                    }
            });
            if (bNewMsg) _getUserList(iChannelId); // update userList once
        }
    });
}

function _getUserList(iChannelId) {
    // TODO: clear "old" user list so that only online users (10 minute rule) are displayed
    $.ajax({
        dataType: "json",
        url: String.format('{0}/channels/{1}/users', g_sServer, iChannelId),
        type:"GET",
        headers: {"X-Group-Token": g_sToken},
        success: function (raw) {
            if (raw === []) return false;
            // if (g_iDebugMsg >= 2) console.log(String.format("[{0}] ", raw));
            $.each(raw, function (index, val) {
                if (g_iDebugMsg >= 2) console.log(String.format("[{0}] User: {1}", index, val));
                if (!_containsElem(g_sUserList, val)) _addUserToScreen(val);
            });
        }
    });
}

//#endregion server funcs


//#region local funcs

function _addMsgToScreen(sMsg, sSender, timestamp = new Date(), bReverse = false) {
        /*
            Function:    _addMsgToScreen
            Description: Adds an element (message + sender) to the chat window
            Params:      sMsg    - Message as string
                         sSender - Sender as string
            Returns:     Nothing.
         */

    let sTime = timestamp.toLocaleString();
    if (sMsg === '' || sSender === '') return;// let sAddString = "<article><div class='user'><p>" + sSender + ":</p></div><div class='msg'><div class='inner-msg'><p>"+ sMsg +"</p></div><div class='timestamp'><p>"+sTime+"</p></div></div></article>";
    let sAddString = String.format("<article><div class='user'><p>{0}:</p></div><div class='msg'><div class='inner-msg'><p>{1}</p></div><div class='timestamp'><p>{2}</p></div></div></article>", sSender, sMsg, sTime);
    let oMsg = $("#chat-screen #messages");
    if (bReverse) { // when loading messages from server we get the newest element first -> reverse adding order
        oMsg.prepend(sAddString);
    } else {
        oMsg.append(sAddString);
    }
    oMsg.scrollTop(oMsg[0].scrollHeight); // scroll to the bottom
    $("#chat-screen .write-msg textarea").focus(); // set focus to message input
}

function _addUserToScreen(sUsername) {
    if (sUsername === '') return;
    let sAddString = String.format("<li><div class='user-list'><div class='user-name'><p>{0}</p></div></div></li>", sUsername);
    $("#users ul").append(sAddString);
}

function _addChannelToScreen(iID, sChannel, sTopic = ''){
    if (sChannel === '') return;
    sChannel = _prepChannelName(sChannel); // trim any unneeded whitespace
    if (sTopic === '') sTopic = 'no topic'; // set default topic
    // prevent code injection
    // add channel to list
    let sAddChannel = String.format("<li><div class='channel-list'><div class='channel-name'><p>{0}</p></div><div class='topic'><p>{1}</p></div></div></li>", sChannel, sTopic);//.data('id', iID);
    $("#channels ul").append(sAddChannel);
    // set channel id
    let oChannel = _getElemByText($("#channels .channel-name p"), sChannel);
    oChannel.data('id', iID);
    if (g_iDebugMsg >= 2) console.log(String.format("[{0}] {1}: {2}", iID, sChannel, oChannel.data('id')));
}


function _switchChannel(sChannel) {
    $("#channels .channel-name p.selected").removeClass("selected"); // remove class selected from currently selected channe
    let oChannel = _getElemByText($("#channels .channel-name p"), _prepChannelName(sChannel));
    let iChannelId = oChannel.data('id');
    oChannel.addClass("selected");

    console.log(String.format("Switching channel to '{0}' with id {1}", sChannel, iChannelId));

    // show channel messages
    _clearChatScreen();
    _getMessages(iChannelId);
    g_curChannel = iChannelId;
}

function _prepChannelName(sChannel) {
    /*
        Function:    _prepChannelName
        Description: prepares the name of a channel (as string) to be added into the list
        Params:      sChannel - String
        Returns:     Updated String
     */

    if (typeof sChannel !== 'string') return 'invalid_ChannelName';
    sChannel = sChannel.trim(); // trim any unneeded whitespace
    // block code injection
    return sChannel.replace(/(?:<script>)?(?:alert)\(?'?(.+?)'\)?(?:<\/script>)?/, "$1");
}

function _clearChatScreen() {
    $("#chat-screen #messages").html("");
}

/*function _convertISOTime(sTime) { // unnecessary
    let timestamp = new Date(sTime);
    // console.log(timestamp);
    return timestamp;
}*/

//#endregion local funcs

//#region Internal Funcs

/*
    Function:    _containsElem
    Description: Checks if an element (string) is in an iterable object list (checks for text).
    Params:      sElemListObject - String to Iterable Object containing the list with all currently displayed elements
                 sSearch         - Element to check if in list
    Returns:     Success         - True if found / else false
*/
function _containsElem(sElemListObject, sSearch) {
    return !!_getElemByText($(sElemListObject), sSearch).length;
}

/*
    Function:    _getElemByText
    Description: Returns the element in the subtree object that equals the search string
    Params:      oTree   - jQuery object to the subtree that contains the searched element
                 sSearch - Element to check if in list
    Returns:     Success - jQuery object to the element / else - untested lel git gud
*/
function _getElemByText(oTree, sSearch){
    return oTree.filter(function () {return $(this).text() === sSearch})
}

function _inputHelper_ReadTextarea() {
    let o = $("#chat-screen .write-msg textarea");
    let sMsg = o.val(); // read msg from input field
    if (sMsg === '') return;
    _addMsgToScreen(sMsg, g_sUsername);
    o.val(''); // clear msg field
}

// internal string formatter
String.format = function() {
    let s = arguments[0];
    for (let i = 0; i < arguments.length - 1; i++) {
        let reg = new RegExp("\\{" + i + "\\}", "gm");
        s = s.replace(reg, arguments[i + 1]);
    }
    return s;
};
//#endregion Internal Funcs