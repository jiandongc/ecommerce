password-reset(name, link) ::= <<
<!DOCTYPE html>
<html>
<head>
</head>
<body style="background-color: #f6f6f6;">

<div class="container" style="background-color: #ffffff; max-width: 600px; margin: 0 auto; overflow: hidden; border-radius: 5px; font-family: sans-serif;">
    <div class="header" style="background-color: #f6f6f6; padding-bottom: 10px; padding-top: 10px;">
        <img src="https://lh3.googleusercontent.com/n7geLROEAZMDDkCRSN7ztgze9D-hAIznjkAuiTrYGKbsGitkSxjn5xn_0k-1rxLpU7idXBi7OkLgo0w_U1SSqyF5-Q8jp2n00E5-sIrhLveUussQ-95uxga2r7cCkpiZ4YCT11nMQr7foEEjWhwYgzcADS6l4MTozH4xtc8GCYuxzj28nchjZKXvuWjpH5FdE6VVDXSHKO2hdIgjsqCuY6LwRJ55ToEUCYRf3lN_WprmruQTE6Q3vKpkWN-RK273Ua8S4TD57oXXesnvF4p-jfAXZnS6n9DrTPObLjlZ_UYlxGR6kQu8JznAYVEmSBOyceaSGs5SNcTkrHqgplq3wBG-7I8HazKC5uwsmkOJv9khvmEQFFffF2p-fC3c1zSfLRt8RcFGJ2iZxRmejvk_LYRCNyx8viqJd3GTxrHn93BF2n1EbfHwJRXu7CNJzTgqc7MGHIGYVzS7MK19JChnF5rKLXWAd-BNZRdbBDxuGhSWl4fB1buRymSX0JesCEcY9DTsOgjP3P3-YdLKwlGN_TKL0EHWOXCWUVMMSWg-9ohFjVifgr44u2oA7b3FBf6_DAAaHZu0Yse16aeFnrTlAcbIGVINHC-Hdn-_BEBOVqLhXtv4YOTDKpQ5FKBUwGSfJ0j3A0W8ZCp_ounn6solkARQKKafYXZMddfNixyBjtaqZug9WKuZ7Q=w1000-h647-no" style="max-height: 40px;">
    </div>
    <div class="message" style="margin: 20px; font-size: 15px; font-weight: 300; color: grey; line-height: 1.5;">
        <p>Hello $name$,</p>
        <p>We received a request to reset your password. If you didn't make the request, please contact us immediately.</p>
        <p>If you did, just click the button below and you will be on your way. This password reset link is only valid for 30 minutes.</p>
        <div class="button" style="text-align: center; margin: 20px;">
            <a href="$link$" style="font-size: 15px; text-decoration: none; display: inline-block; background: #f0c14b; color: white; padding: 10px 20px 10px; border-radius: 5px; text-align: center;">Reset your password</a>
        </div>
        <p>Kind regards,<br>Cawaii Customer Service</p>
    </div>
    <div class="footer" style="padding-top: 30px; background-color: #f6f6f6; text-align: center; color: grey; font-size: 12px; letter-spacing: 0.8px;">
        @ 2019 CAWAII. INC.<br>
        ALL RIGHTS RESERVED
    </div>
</div>

</body>
</html>
>>