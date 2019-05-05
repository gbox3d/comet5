
export {comet5}

function comet5() {


  //fcm
  if(global.fcmInterface === undefined) {

    console.log('emulate fcm interface')

    this.fcmInterface = {
      getToken : ({onCallback})=> {

        onCallback("xxxxxxxitisfcmtokentestxxxxxxxxx")

        // if(this.fcmInterface.OnCallback !== undefined) {
        //   this.fcmInterface.OnCallback("xxxxxxx")
        // }
      }
    }

  }
  else {

    console.log('find fcm interface')

    this.fcmInterface = {
      getToken : ({onCallback})=> {
        fcmInterface.OnCallback = onCallback
        fcmInterface.getFcmToken('')
      }
    }

  }

  //더미 객체 초기화
  if(global.dummyWIO) {
    console.log('find dumwIO interface')
    this.dummyWIO = {
      Test : ({msg,onCallback})=> {
        dummyWIO.OnCallback = onCallback
        dummyWIO.Test(msg);
      }
    }

  }
  else {
    console.log('emulate dumyWIO interface')
    this.dummyWIO = {
      Test : ({msg,onCallback})=> {
        onCallback(msg)
      }
    }

  }

  //gps
  if(global.WIOgps) {
    console.log('find WIOgps interface')

    this.WIOgps = {

      getLastLocation : ({onCallback,onErr})=> {
        WIOgps.OnCallbackGps = onCallback
        WIOgps.OnErrorGps = onErr
        WIOgps.getLastLocation()
      }
    }

  }
  else {
    console.log('emulate WIOgps interface')
    this.WIOgps = {

      getLastLocation : ({onCallback,onErr})=> {
        onCallback({
          accuracy : 5,
          latitude : 35.865907,
          longitude : 126.8576322481264,
          time : new Date()
        })

      }
    }

  }


}


//global.commet5 = _commet5