import { Template } from 'meteor/templating';
import { ReactiveVar } from 'meteor/reactive-var';

import {comet5} from "./comet5";

import './main.html';

Template.hello.onCreated(function helloOnCreated() {
  // counter starts at 0
  this.counter = new ReactiveVar(0);
  this.comet5 = new comet5()
});

Template.hello.helpers({
  counter() {
    return Template.instance().counter.get();
  },
});

Template.hello.events({

  'click [name="dummyWIO"] button'(event, instance) {

    console.log('test')

    instance.comet5.dummyWIO.Test({
      msg : "hello comet5",
      onCallback : (_)=> {
        instance.find('[name="dummyWIO"] [name="output"]').innerText = _

        instance.counter.set(instance.counter.get() + 1);
      }
    })
  },
  'click [name="WIOgps"] button'(event, instance) {

    instance.comet5.WIOgps.getLastLocation({
      onCallback : (_)=> {
        console.log(_)
        instance.find('[name="WIOgps"] [name="output"]').innerText = `lat : ${_.latitude} lng : ${_.longitude}`
      }
    })

  },

  'click [name="fcmInterface"] button'(event, instance) {


    instance.comet5.fcmInterface.getToken({
      onCallback : (_)=> {
        console.log(_)
        instance.find('[name="fcmInterface"] [name="output"]').innerText = _

      }
    })

  },
});
