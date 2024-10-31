/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.hmrcemailrenderer.templates.dprs

import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.hmrcemailrenderer.domain.{ MessagePriority, MessageTemplate }
import uk.gov.hmrc.hmrcemailrenderer.templates.FromAddress.govUkTeamAddress
import uk.gov.hmrc.hmrcemailrenderer.templates.ServiceIdentifier.DigitalPlatformReporting
import uk.gov.hmrc.hmrcemailrenderer.templates.{ CommonParamsForSpec, FromAddress }

class AddedReportingNotificationSpec extends PlaySpec with CommonParamsForSpec {

  "dprs_added_reporting_notification" must {
    val underTest = MessageTemplate.create(
      templateId = "dprs_added_reporting_notification",
      fromAddress = govUkTeamAddress,
      service = DigitalPlatformReporting,
      subject = "You have added a reporting notification in the digital platform reporting service",
      plainTemplate = txt.dprs_added_reporting_notification.f,
      htmlTemplate = html.dprs_added_reporting_notification.f,
      priority = Some(MessagePriority.Urgent)
    )

    "include correct subject" in {
      underTest.subject(
        commonParameters
      ) mustBe "You have added a reporting notification in the digital platform reporting service"
    }

    "include htmlTemplate body and footer" in {
      val htmlContent = underTest
        .htmlTemplate(
          commonParameters ++ Map(
            "userPrimaryContactName" -> "Ashley Smith",
            "poBusinessName"         -> "Vinted",
            "poId"                   -> "PO12345678"
          )
        )
        .toString
      htmlContent must include("You have added a reporting notification")
      htmlContent must include("Dear")
      htmlContent must include("Ashley Smith")
      htmlContent must include(
        "You have successfully added a reporting notification for Vinted in the digital platform reporting service."
      )
      htmlContent must include("What you need to do next")
      htmlContent must include(
        "If required, you can now submit reports for this platform operator. Go to GOV.UK and search for 'reporting rules for digital platforms'."
      )
      htmlContent must include("For security reasons, we have not included a link to this service in this email.")
      htmlContent must include("For more information, search GOV.UK for 'reporting rules for digital platforms'.")
      htmlContent must include("From the HMRC Digital Platform Reporting team")
    }
  }
}