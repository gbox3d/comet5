
export {comet5}

function comet5() {

  //let _interface = {}

  //console.log(dummWIO)

  if(global.fcmInterface === undefined) {

    console.log('emulate fcm interface')

    this.fcmInterface = {
      getToken :  ()=> {

        if(this.fcmInterface.OnCallback !== undefined) {
          this.fcmInterface.OnCallback("xxxxxxx")
        }
      },
      setCallback : (cb) => {
        this.fcmInterface.OnCallback = cb
      }
    }

  }
  else {

    console.log('find fcm interface')

    // fcmInterface.OnCallback = _=>{
    //   console.log(_)
    // }
    //
    // fcmInterface.getFcmToken('')

    this.fcmInterface = {
      getToken : ()=> {
        fcmInterface.getFcmToken('')
      },
      setCallback : (cb) => {
        fcmInterface.OnCallback = cb
      }
    }

  }

  //더미 객체 초기화
  if(global.dummyWIO) {
    console.log('find dumwIO interface')
    this.dummyWIO = {
      Test : (msg)=> {
        dummyWIO.Test(msg);
      },
      setCallback : ({OnCallback})=> {
        dummyWIO.OnCallback = OnCallback
      }
    }

  }
  else {
    console.log('emulate dumyWIO interface')
    this.dummyWIO = {
      Test : (msg)=> {
        if(this.OnCallback) this.OnCallback(msg)
      },
      setCallback : ({OnCallback})=> {
        this.OnCallback = OnCallback
      }
    }

  }

  //gps
  if(global.WIOgps) {
    console.log('find WIOgps interface')

    this.WIOgps = {

      setCallback : ({OnCallback,OnErr})=> {
        WIOgps.OnCallbackGps = OnCallback
        WIOgps.OnErrorGps = OnErr
      },
      getLastLocation : ()=> {
        WIOgps.getLastLocation()
      }
    }

  }
  else {
    console.log('emulate WIOgps interface')
    this.WIOgps = {

      setCallback : ({OnCallback,OnErr})=> {
        this.OnCallbackGps = OnCallback
        this.OnErrorGps = OnErr
      },
      getLastLocation : ()=> {
        if(this.OnCallbackGps) {
          this.OnCallbackGps({
            accuracy : 100,
            latitude : 35.865907,
            longitude : 126.8576322481264,
            time : new Date()
          })
        }
      }
    }

  }


}


//global.commet5 = _commet5