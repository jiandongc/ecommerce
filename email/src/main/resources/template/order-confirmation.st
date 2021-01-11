order-confirmation(customerName, orderNumber, orderEta, orderDeliveryMethod, shippingAddress, orderItems, guest, siteName, homePage, registrationPage) ::= <<
<!DOCTYPE html>
<html>
<head>
</head>
<body style="background-color: #f6f6f6;">

<div class="container" style="background-color: #ffffff; max-width: 600px; margin: 0 auto; overflow: hidden; border-radius: 5px; font-family: sans-serif;">
    <div class="header" style="background-color: #f6f6f6; padding-bottom: 10px; padding-top: 10px;">
        <img src="https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/logo_grey.png" style="max-height: 40px;">
    </div>
    <div class="message" style="margin: 20px; line-height: 1.5;">
        <div style="overflow: auto; color: #333; font-size: 14px;">
            <div style="float: right;">
                <div style="font-size: 1.3em; font-weight: bold; float: right;">Order Confirmation</div>
                <div style="font-weight: 300;">Order #<a href="">$orderNumber$</a></div>
            </div>
        </div>

        <div style="font-size: 14px; margin-bottom: 0.5em;"><span style="color:#f0c14b;">Hello,</span> <a href="">$customerName$</a></div>
        <div style="font-size: 14px; font-weight: bold; margin-bottom: 0.5em;">
            Thanks for your order. Your estimated delivery date is indicated below.<br/>
            $if(guest)$
            Would you like to create an account with us to track your order on $siteName$? <a href="$registrationPage$$orderNumber$">Click here to create account</a>
            $else$
            You can view the status of your order by visiting Your Orders on <a href="$homePage$">$siteName$</a>
            $endif$
        </div>

        <div style="background-color: #E8E8E8; margin-bottom: 0.5em;">
            <table style="width:100%; font-size: 14px;">
              <tr>
                <td style="padding-left: 10px; width: 50%">
                    <div style="color:#505050;">Arriving:</div>
                    <div style="color:#009933; font-weight: bold; margin-bottom: 0.5em;">$orderEta$</div>
                </td>
                <td rowspan="2" style="vertical-align: top; padding-left: 10px; width: 50%">
                    <div style="color:#505050;">Your order will be sent to:</div>
                    <div style="font-weight: bold;">$shippingAddress.title$ $shippingAddress.name$</div>
                    <div style="font-weight: bold;">$shippingAddress.addressLine1$</div>
                    <div style="font-weight: bold;">$shippingAddress.addressLine2$</div>
                    <div style="font-weight: bold;">$shippingAddress.city$, $shippingAddress.postcode$</div>
                    <div style="font-weight: bold;">$shippingAddress.country$</div>
                </td>
              </tr>
              <tr>
                <td style="vertical-align: top; padding-left: 10px; width: 50%">
                    <div style="color:#505050;">Your delivery option:</div>
                    <div style="font-weight: bold; margin-bottom: 0.5em;">$orderDeliveryMethod$</div>
                </td>
              </tr>
            </table>
        </div>

        <div style="color: #f0c14b; font-weight: bold; font-size: 14px; border-bottom: 2px solid #E8E8E8; width: 100%; display: block; margin-bottom: 0.5em;">
            Order Details
        </div>

        <div style="margin-bottom: 0.5em;">
            <table style="width:100%; font-size: 14px;">

            $orderItems :{ orderItem |
              <tr style="margin-bottom: 0.5em;">
                <td width="30%">
                    <div class="col-xs-6">
                        <img style="display: block; padding: 4px; background-color: #fff; border: 1px solid #ddd; border-radius: 4px; vertical-align: middle; max-width: 100%; height: auto;" src="$orderItem.imageUrl$">
                    </div>
                </td>
                <td style="vertical-align: top;" width="70%">
                    <div style="padding-left: 30px;">
                        <span style="color: #4c4445; font-size: 1.0em; font-weight: bold;">$orderItem.name$</span>
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 200;">#$orderItem.sku$</span>
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 300;">$orderItem.description$</span>
                    </div>
                    <div style="padding-left: 30px;">
                        <span style="font-weight: 200;">Quantity: </span>$orderItem.quantity$
                    </div>
                </td>
              </tr>
              }$

            </table>
        </div>

    </div>
    <div class="footer" style="padding-top: 30px; padding-bottom: 15px; background-color: #f6f6f6; text-align: center; color: grey; font-size: 12px; letter-spacing: 0.8px;">
        @ 2021 Noodle-Monster. INC.
    </div>
</div>

</body>
</html>
>>