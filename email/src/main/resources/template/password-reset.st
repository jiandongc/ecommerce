password-reset(name, link) ::= <<
<!DOCTYPE html>
<html>
<head>
</head>
<body style="background-color: #f6f6f6;">

<div class="container" style="background-color: #ffffff; max-width: 600px; margin: 0 auto; overflow: hidden; border-radius: 5px; font-family: sans-serif;">
    <div class="header" style="background-color: #f6f6f6; padding-bottom: 10px; padding-top: 10px;">
        <img src="https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/logo_grey.png" style="max-height: 40px;">
    </div>
    <div class="message" style="margin: 20px; font-size: 15px; font-weight: 300; color: grey; line-height: 1.5;">
        <p>Hello $name$,</p>
        <p>We received a request to reset your password. If you didn't make the request, please contact us immediately.</p>
        <p>If you did, just click the button below and you will be on your way. This password reset link is only valid for 30 minutes.</p>
        <div class="button" style="text-align: center; margin: 20px;">
            <a href="$link$" style="font-size: 15px; text-decoration: none; display: inline-block; background: #f0c14b; color: white; padding: 10px 20px 10px; border-radius: 5px; text-align: center;">Reset your password</a>
        </div>
        <p>Kind regards,<br>Noodle-Monster Customer Service</p>
    </div>
    <div class="footer" style="padding-top: 30px; padding-bottom: 15px; background-color: #f6f6f6; text-align: center; color: grey; font-size: 12px; letter-spacing: 0.8px;">
        @ 2021 Noodle-Monster. INC.
    </div>
</div>

</body>
</html>
>>