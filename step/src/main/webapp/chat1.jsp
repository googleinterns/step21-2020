<html>
<head> 
    <title> Friend Matching Plus </title>
    <link rel="stylesheet" href="style_chat1.css"> 
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<body>
    <div id="chat-container">
        <div id="search-container">
            <input type="text" placeholder="Search" />
        </div>
        <div id="conversation-list"> 
            <div class="conversation active">
                <img src="avatar.png" alt="Person 1" />
                <div class="title-text">
                    Person 1
                </div>
                <div class="created-date">
                    Apr 16
                </div>
                <div class="conversation-message"> 
                    This is a message 
                </div>
            </div>

            <div class="conversation">
                <img src="avatar.png" alt="Person 2" />
                <div class="title-text">
                    Person 2
                </div>
                <div class="created-date">
                    Apr 16
                </div>
                <div class="conversation-message"> 
                    This is a message 
                </div>
            </div>

            <div class="conversation">
                <img src="avatar.png" alt="Person 3" />
                <div class="title-text">
                    Person 3
                </div>
                <div class="created-date">
                    Apr 16
                </div>
                <div class="conversation-message"> 
                    This is a message 
                </div>
            </div>
        </div>
        <div id="new-message-container"> 
            <a href="#"> </a>
        </div>
        <div id="chat-title">
            <span> Person 1</span>
            <img src="avatar.png" alt="Your match's avatar" />
        </div>
        <div id="chat-message-list"> 
            <div class="message-row your-message">
                <div class="message-content">
                    <div class="message-text">Hi! Nice to meet you too!</div>
                    <div class="message-time">Apr 16</div>
                </div>
            </div>
            <div class="message-row other-message">
                <div class="message-content">
                    <img src="avatar.png" alt="My Avatar"/>
                    <div class="message-text">
                        Hi, I am Person 1. Nice to meet you!
                    </div>
                    <div class="message-time">Apr 16</div>
                </div>
            </div>
        </div>
        <div id="chat-form"> 
            <i class="fa fa-paperclip"></i>
            <input type="text" placeholder="type a message" />
        </div>
    </div>
</body>
</html>