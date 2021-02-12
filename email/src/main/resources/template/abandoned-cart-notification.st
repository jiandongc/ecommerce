abandoned-cart-notification(cartUid, cartItems) ::= <<
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
        <div style="text-align: center;">We noticed you left something behind. No need to worry - we saved the items in your cart so you can easily complete your purchase.</div>
        <br/>
        <div style="margin-bottom: 0.5em;">
            <table style="width:100%; font-size: 14px;">
            $cartItems :{ cartItem |
              <tr style="margin-bottom: 0.5em;">
                <td width="30%">
                    <div class="col-xs-6">
                        <img style="display: block; padding: 4px; background-color: #fff; border: 1px solid #ddd; border-radius: 4px; vertical-align: middle; max-width: 100%; height: auto;" src="$cartItem.imageUrl$">
                    </div>
                </td>
                <td style="vertical-align: top;" width="70%">
                    <div style="padding-left: 30px;">
                        <span style="color: #4c4445; font-size: 1.0em; font-weight: bold;">$cartItem.name$</span>
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 300;">$cartItem.description$</span>
                    </div>
                    <div style="padding-left: 30px; padding-top: 10px">
                        <span style="font-weight: 200;">Quantity: </span>$cartItem.quantity$
                    </div>
                </td>
              </tr>
              }$
            </table>
        </div>
        <div class="button" style="text-align: center; margin: 20px;">
            <a href="https://noodle-monster.co.uk/cart/$cartUid$" target="_blank"
               style="font-size: 15px; text-decoration: none; display: inline-block; background: #f0c14b; color: #002E36; padding: 10px 20px 10px; border-radius: 5px; text-align: center;">Return to your cart</a>
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