// @flow

import AppToaster from '../app/AppToaster'
import { IToastProps } from '@blueprintjs/core'

export function toastifyError (error: Error, toastProps?: IToastProps) {
  console.error(error)
  AppToaster.show({
    icon: 'error',
    message: error.message,
    intent: 'danger',
    ...toastProps
  })
}
