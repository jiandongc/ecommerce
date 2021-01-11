welcome-email(voucherAmount, voucherCode) ::= <<
<!DOCTYPE html>
<html>
<head>
</head>
<body style="background-color: #f6f6f6;">

<div class="container" style="background-color: #ffffff; max-width: 600px; margin: 0 auto; overflow: hidden; border-radius: 5px; font-family: sans-serif;">
    <div class="header" style="background-color: #f6f6f6; padding-bottom: 10px; padding-top: 10px;">
        <img src="https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/logo_grey.png" style="max-height: 40px;">
    </div>
    <div class="message" style="margin: 20px; font-size: 15px; font-weight: 300; color: #002E36; line-height: 1.5;">
        <div style="text-align: center; margin: 20px;">
            <img src="https://s3-eu-west-1.amazonaws.com/noodle-monster.co.uk/images/the-munchies.png" width="200"
                 height="163">
        </div>
        <div style="text-align: center;">Thanks for joining Noodle Monster! As a token of our appreciation,</div>
        <div style="text-align: center; font-weight: bold; font-size: 20px;">TAKE $voucherAmount$% OFF</div>
        <div style="text-align: center; margin-bottom: 15px;">On your next order with exclusive voucher code:</div>
        <div style="text-align: center; font-weight: bold;">$voucherCode$</div>
        <div class="button" style="text-align: center; margin: 20px;">
            <a href="https://noodle-monster.co.uk"
               style="font-size: 15px; text-decoration: none; display: inline-block; background: #f0c14b; color: #002E36; padding: 10px 20px 10px; border-radius: 5px; text-align: center;">Shop Tasty Noodles</a>
        </div>
        <p style="color: grey;">Kind regards,<br>Noodle-Monster Customer Service</p>
    </div>
    <div class="footer" style="padding-top: 30px; padding-bottom: 15px; background-color: #f6f6f6; text-align: center; color: grey; font-size: 12px; letter-spacing: 0.8px;">
        @ 2021 Noodle-Monster. INC.
    </div>
</div>

</body>
</html>
>>